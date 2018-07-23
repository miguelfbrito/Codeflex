package pt.codeflex.evaluatesubmissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.xfer.FileSystemFile;
import pt.codeflex.controllers.DatabaseController;
import pt.codeflex.databasemodels.Durations;
import pt.codeflex.databasemodels.Leaderboard;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Result;
import pt.codeflex.databasemodels.Scoring;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.TestCases;
import pt.codeflex.databasemodels.Tournament;
import pt.codeflex.models.Host;
import pt.codeflex.repositories.LeaderboardRepository;
import pt.codeflex.repositories.ResultRepository;
import pt.codeflex.repositories.ScoringRepository;
import pt.codeflex.repositories.SubmissionsRepository;

import static pt.codeflex.evaluatesubmissions.EvaluateConstants.PATH_SPRING;
import static pt.codeflex.evaluatesubmissions.EvaluateConstants.PATH_SERVER;
import static pt.codeflex.evaluatesubmissions.EvaluateConstants.PATH_FIREJAIL;
import static pt.codeflex.evaluatesubmissions.EvaluateConstants.CLASS_FILE_NAME;

@Component
@Transactional
@Scope("prototype")
public class EvaluateSubmissions implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(EvaluateSubmissions.class.getName());

	@Autowired
	private DatabaseController db;

	@Autowired
	private ScoringRepository scoringRepository;

	@Autowired
	private SubmissionsRepository submissionsRepository;

	@Autowired
	private ResultRepository resultRepository;

	@Autowired
	private LeaderboardRepository leaderboardRepository;

	private Host host;
	private long uniqueId;
	private static Queue<Submissions> submissionsQueue = new ArrayDeque<Submissions>();

	@Override
	public void run() {
		LOGGER.log(Level.FINE, "Starting evaluation!");
		distributeSubmissions();
	}

	public synchronized void distributeSubmissions() {
		while (!submissionsQueue.isEmpty()) {
			Submissions submission = submissionsQueue.poll();
			compileSubmission(submission);
		}
	}

	public List<Submissions> getSubmissions() {

		List<Submissions> submissions = submissionsRepository.findSubmissionsToAvaliate();
		List<Submissions> finalSubmissions = new ArrayList<>();

		for (Submissions s : submissions) {
			Optional<Submissions> submission = submissionsRepository.findById(s.getId());
			if (submission.isPresent() && !submissionsQueue.contains(submission.get())) {
				submissionsQueue.add(submission.get());
			}
		}

		return finalSubmissions;

	}

	public void compileSubmission(Submissions submission) {
		Problem problem = submission.getProblem();

		LOGGER.log(Level.FINE, submission.toString());

		uniqueId = submission.getId();
		Session session = null;

		// TODO : fix when it doesn't connect. Should keep trying for a fixed amount of
		// time
		try {
			session = host.getSsh().startSession();
		} catch (ConnectionException | TransportException e2) {
			e2.printStackTrace();
		}

		CompilationInfo compilationInfo = CommandGeneration.compilation(submission);

		String compilerError = EvaluateConstants.compilerErrorFile(submission.getId());
		String command = compilationInfo.getCommand();
		String suffix = compilationInfo.getLanguageSuffix();

		Command cmd = null;
		try {
			cmd = session.exec("mkdir " + PATH_SERVER + "/" + uniqueId + "_" + submission.getLanguage().getName());
			cmd.close();

		} catch (ConnectionException | TransportException e1) {
			e1.printStackTrace();
		}

		// Create file with the code and send it to the server
		createFile(new String(Base64.getDecoder().decode(submission.getCode())), "Solution");
		scp(PATH_SPRING + File.separator + CLASS_FILE_NAME, PATH_SERVER + File.separator + uniqueId + "_"
				+ submission.getLanguage().getName() + File.separator + "Solution" + suffix);

		// Compiles the code using the command generated previously

		try {
			session = host.getSsh().startSession();
			cmd = session.exec(command);
			cmd.close();

		} catch (ConnectionException | TransportException e) {
			e.printStackTrace();
		}
		
		
		// Verifica se houve erro
		try {
			session = host.getSsh().startSession();
			command = "cat " + PATH_SERVER + "/" + uniqueId + "_" + submission.getLanguage().getName() + "/"
					+ compilerError;
			cmd = session.exec(command);
			String output = IOUtils.readFully(cmd.getInputStream()).toString();

			if (!output.equals("")) {
				List<Result> current = resultRepository.findAllByName("Compiler Error");

				boolean exists = false;
				for (Result r : current) {
					if (r.getMessage() != null && r.getMessage().equals(output)) {
						exists = true;
						submission.setResult(r);
					}
				}

				if (!exists) {
					Result r = new Result("Compiler Error", output);
					resultRepository.save(r);
					submission.setResult(r);
				}

				Scoring sc = new Scoring(submission, new TestCases(), 0, 0);
				scoringRepository.save(sc);

				submissionsRepository.save(submission);
				System.out.println("Compiler error!");
				return;
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
		
		
		
		
		
		
		

		List<TestCases> testCases = submission.getProblem().getTestCases();
		String commandsToExecute = "";
		String dirName = submission.getId() + "_" + submission.getLanguage().getName() + "/";
		String obtainOutput = "(";

		for (TestCases tc : testCases) {

			createFileInFolder(tc.getInput(), dirName, String.valueOf(tc.getId()));

			commandsToExecute += getRunCommand(submission, tc) + "\n";
			obtainOutput += " echo '%%%output_" + tc.getId() + "' && cat output_" + submission.getId() + "_"
					+ tc.getId() + ".txt && echo 'end%%%' &&";
		}
		scp(PATH_SPRING + "/" + dirName,
				PATH_SERVER + "/" + submission.getId() + "_" + submission.getLanguage().getName() + "/");

		// creates jobs to be executed by parallel
		createFile(commandsToExecute, "jobs.txt");
		scp(PATH_SPRING + "/" + "jobs.txt",
				PATH_SERVER + "/" + submission.getId() + "_" + submission.getLanguage().getName() + "/");

		// removes the last unecessary &&
		if (obtainOutput.length() > 2) {
			obtainOutput = obtainOutput.substring(0, obtainOutput.length() - 2);
		}
		obtainOutput += " ) 2> err";

		// executes submissions in parallelism

		String output = runTestCasesForSubmission(submission, obtainOutput);
		System.out.println("\n\n\n" + output + "\n\n\n");

		//
		// Evaluation
		//

		int totalTestCasesForProblem = problem.getTestCases().size();
		int givenTestCases = 0;

		// checks all testcases
		for (TestCases tc : testCases) {
			System.out.println("INPUT " + tc.getInput());
			String tcName = "%%%output_" + tc.getId();
			String s = output;
			if (!s.equals("")) {
				s = s.substring(s.indexOf(tcName));
				s = s.substring(0, s.indexOf("end%%%"));
				s = s.replace(tcName, "").trim();
			}

			System.out.println("TESTCASE " + tc.getId());
			System.out.println(s.trim());

			// Evaluates test case
			int isRight = validateResult(tc.getOutput(), s);

			if (tc.isShown()) {
				givenTestCases++;
			}

			double score = isRight == 1
					? ((double) submission.getProblem().getMaxScore()
							/ ((double) totalTestCasesForProblem - (double) givenTestCases))
					: 0;
			System.out.println("Score " + score);
			Scoring sc = new Scoring(submission, tc, score, isRight);
			scoringRepository.save(sc);

		}

		List<Scoring> scoringBySubmission = scoringRepository.findAllBySubmissions(submission);
		int totalScoring = scoringBySubmission.size();
		int countCorrectScoring = 0;
		if (totalScoring == totalTestCasesForProblem) {
			double totalScore = 0;
			for (Scoring s : scoringBySubmission) {
				if (s.getIsRight() == 1) {
					countCorrectScoring++;
				}
				totalScore += s.getValue();
			}

			if (countCorrectScoring == totalTestCasesForProblem) {
				System.out.println("Correct problem!");
				submission.setResult(resultRepository.findByName("Correct"));

				// Updates the completion date in order to calculate how much time a user
				// took
				// // to solve the problem
				Durations currentDuration = db.viewDurationsById(submission.getUsers().getId(),
						submission.getProblem().getId());
				db.updateDurationsOnProblemCompletion(currentDuration);
			} else {
				submission.setResult(resultRepository.findByName("Incorrect"));
			}
			submission.setScore(totalScore);
			submissionsRepository.save(submission);

			List<Leaderboard> highestSubmissionOnLeaderboard = leaderboardRepository
					.findHighestScoreByUserByProblem(submission.getUsers(), submission.getProblem());

			Leaderboard newLeaderboard = new Leaderboard(totalScore, submission.getUsers(), submission.getProblem(),
					submission.getLanguage().getName());

			Tournament currentTounament = submission.getProblem().getTournament();
			// if the tournament has closed already, won't change the leaderboard.
			if (currentTounament != null && !currentTounament.getOpen()) {
				System.out.println("ESTÁ FECHADO");
				try {
					cmd.close();
				} catch (TransportException | ConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}

			if (!highestSubmissionOnLeaderboard.isEmpty()) {

				Leaderboard maxScoreSubmission = highestSubmissionOnLeaderboard.get(0);

				// ugly, can't figure out how to load an object using only the id from the
				// previous query
				Optional<Leaderboard> currentLeaderboard = leaderboardRepository.findById(maxScoreSubmission.getId());

				if (totalScore > maxScoreSubmission.getScore()) {
					if (maxScoreSubmission.getScore() == -1) {
						maxScoreSubmission = newLeaderboard;
					} else {
						if (currentLeaderboard.isPresent()) {
							maxScoreSubmission = currentLeaderboard.get();
							maxScoreSubmission.setScore(totalScore);
						}
					}
					leaderboardRepository.save(maxScoreSubmission);
				}
			} else {
				leaderboardRepository.save(newLeaderboard);
			}
		}

	}

	public String runTestCasesForSubmission(Submissions submission, String obtainOutput) {

		Session session = null;
		try {
			session = host.getSsh().startSession();
		} catch (ConnectionException | TransportException e) {

		}

		String dirName = submission.getId() + "_" + submission.getLanguage().getName();
		String command = " cd " + PATH_SERVER + "/" + dirName + " && parallel -j `nproc` < jobs.txt ; ";
		command = command.concat(obtainOutput);

		try {

			Command cmd = session.exec(command);
			String output = IOUtils.readFully(cmd.getInputStream()).toString();

			System.out.println("OUTPUT " + output);

			cmd.close();
			return output;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public String getRunCommand(Submissions submission, TestCases testCase) {

		String dirName = submission.getId() + "_" + submission.getLanguage().getName();
		String command = "firejail --private=" + PATH_FIREJAIL + "/" + dirName + " --quiet --net=none cat " + dirName
				+ "/" + testCase.getId() + " | ";

		String fileName = "Solution";
		String runError = "runtime_error_" + submission.getId() + ".txt";
		String runOutput = "output_" + submission.getId() + "_" + testCase.getId() + ".txt";
		String outputPath = PATH_FIREJAIL + "/" + dirName + "/" + runOutput;

		switch (submission.getLanguage().getCompilerName()) {
		case "Java 8":
			command += "timeout 3s java " + fileName + " 2> " + runError + " > " + runOutput + "";
			break;
		case "C++11 (gcc 5.4.0)":
			command += " timeout 2 ./" + fileName + "_exec_" + uniqueId + " 2> " + runError + " > " + runOutput + "";
			break;
		case "Python 2.7":
			command += "timeout 10 python " + fileName + ".py 2> " + runError + " > " + runOutput + "";
			break;
		case "C# (mono 4.2.1)":
			command += "timeout 3 ./" + fileName + "_exec_" + uniqueId + " 2> " + runError + " > " + runOutput + "";
			break;
		default:
			break;
		}

		return command;

	}

	private int validateResult(String tcOutput, String output) {
		System.out.println("\n\nComparing results");
		System.out.println(tcOutput + " - " + output + "\n\n\n");

		if (tcOutput.trim().equals(output.trim())) {
			return 1;
		} else if (output.trim().equals("")) {
			return -1;
		}
		return 0;
	}

	public void createFile(String text, String file) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(PATH_SPRING + "/" + file, "UTF-8");
			writer.print(text);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger("Create File").log(Level.SEVERE, "Error creating file", e);
			e.printStackTrace();
		}
	}

	public void createFileInFolder(String text, String folder, String file) {
		PrintWriter writer = null;
		try {
			new File(PATH_SPRING + "/" + folder).mkdirs();
			writer = new PrintWriter(PATH_SPRING + "/" + folder + "/" + file, "UTF-8");
			writer.print(text);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger("Create File").log(Level.SEVERE, "Error creating file", e);
			e.printStackTrace();
		}
	}

	public void scp(String src, String dest) {

		int count = 0;
		int maxCount = 5;
		while (count < maxCount) {
			try {
				System.out.println("Sending file from : " + src + " to " + dest + "\n\n");
				host.getSsh().newSCPFileTransfer().upload(new FileSystemFile(src), dest);
				break;
			} catch (IOException e) {
				e.printStackTrace();
				count++;
				System.out.println("Trying SCP for the " + count + " time. ");
			}
		}
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

}
