package pt.codeflex.evaluatesubmissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.hibernate.boot.model.relational.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.xfer.FileSystemFile;
import pt.codeflex.controllers.DatabaseController;
import pt.codeflex.databasemodels.Leaderboard;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Result;
import pt.codeflex.databasemodels.Scoring;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.TestCases;
import pt.codeflex.databasemodels.Users;
import pt.codeflex.repositories.LeaderboardRepository;
import pt.codeflex.repositories.ResultRepository;
import pt.codeflex.repositories.ScoringRepository;
import pt.codeflex.repositories.SubmissionsRepository;
import pt.codeflex.repositories.UsersRepository;

@Component
@Scope("prototype")
public class EvaluateSubmissions implements Runnable {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private ScoringRepository scoringRepository;

	@Autowired
	private SubmissionsRepository submissionsRepository;

	@Autowired
	private ResultRepository resultRepository;

	@Autowired
	private LeaderboardRepository leaderboardRepository;

	private String host;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	private static int count = 0;
	private static volatile Queue<Submissions> submissionsQueue = new ArrayDeque<>();
	private static final String PATH_SPRING = System.getProperty("user.home") + File.separator + "Submissions";
	private static final String PATH_SERVER = "/home/mbrito/Desktop/Submissions";
	private long uniqueId;

	@Override
	public void run() {
		System.out.println("Thread starting!");
		System.out.println("Connection established!");

		distributeSubmissions();
	}

	public void connect(String host) {
		ssh = new SSHClient();
		ssh.addHostKeyVerifier("33:02:cb:3b:13:b1:bd:fa:66:ff:29:96:ea:ff:dc:78");
		try {
			//ssh.loadKnownHosts();
			ssh.connect(host);
			ssh.authPublickey("mbrito");
			Session session = ssh.startSession();
			Command cmd = session.exec("ls");
			System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());

		} catch (IOException e) {
			// System.out.println("Error connecting!");
			e.printStackTrace();
		}

	}
	
	public void getSubmissions() {

		System.out.println("Connecting thread!");
		List<Submissions> submissions = submissionsRepository.findSubmissionsToAvaliate();
		List<Submissions> listSubmissions = submissionsRepository.findAll();

		for (Submissions s : submissions) {
			Optional<Submissions> submission = submissionsRepository.findById(s.getId());
			if (submission.isPresent()) {
				submissionsQueue.add(submission.get());
			}
		}

		// TESTING PURPOSE TODO : remove
		try {
			if (count++ < 2) {
				Session session = ssh.startSession();
				Command cmd = session.exec("cd " + PATH_SERVER + " && rm -rf *");
				session.close();
			}
		} catch (ConnectionException | TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(submissionsQueue.toString());

	}

	public void distributeSubmissions() {
		while (!submissionsQueue.isEmpty()) {
			Submissions s = submissionsQueue.poll();
			compileSubmission(s);
		}
	}

	public SSHClient ssh = null;


	public void compileSubmission(Submissions submission) {
		uniqueId = submission.getId();
		Session session = null;
		try {
			session = ssh.startSession();
		} catch (ConnectionException | TransportException e2) {
			e2.printStackTrace();
		}

		String fileName = "Solution";
		String suffix = "";
		String compilerError = "compiler_error_" + uniqueId + ".txt";
		String command = "cd Desktop/Submissions/" + uniqueId + "_" + submission.getLanguage().getName() + " && ";
		// TODO : add memory limit

		switch (submission.getLanguage().getCompilerName()) {
		case "Java 8":
			command += "javac " + fileName + ".java 2> " + compilerError + ""; // && disown
			suffix = ".java";
			break;
		case "C++11 (gcc 5.4.0)":
			command += "g++ -std=c++11 -o " + fileName + "_exec_" + uniqueId + " " + fileName + ".cpp 2> "
					+ compilerError + "";
			suffix = ".cpp";
			break;
		case "Python 2.7":
			break;
		case "C# (mono 4.2.1)":
			command += "mcs -out:" + fileName + "_exec_" + uniqueId + " " + fileName + ".cs 2> " + compilerError + "";
			suffix = ".cs";
			break;
		default:
			break;
		}

		Command cmd;
		try {
			cmd = session.exec("mkdir Desktop/Submissions/" + uniqueId + "_" + submission.getLanguage().getName());
			cmd.close();

		} catch (ConnectionException | TransportException e1) {
			e1.printStackTrace();
		}

		// Create and send the code to the server
		createFile(new String(Base64.getDecoder().decode(submission.getCode())), "Solution");
		scp(PATH_SPRING + "/" + fileName,
				PATH_SERVER + "/" + uniqueId + "_" + submission.getLanguage().getName() + "/Solution" + suffix);

		try {
			session = ssh.startSession();
			cmd = session.exec(command);
			cmd.close();

			// Verifica se houve erro
			try {
				session = ssh.startSession();
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
			for (TestCases tc : testCases) {

				String tcFileName = String.valueOf(tc.getId());
				createFile(tc.getInput(), tcFileName);
				scp(PATH_SPRING + "/" + tcFileName,
						PATH_SERVER + "/" + submission.getId() + "_" + submission.getLanguage().getName() + "/");

				runSubmission(submission, tc, fileName);
			}
		} catch (ConnectionException | TransportException e) {
			e.printStackTrace();
		}

	}

	public void runSubmission(Submissions submission, TestCases testCase, String fileName) {

		Session session = null;
		try {
			session = ssh.startSession();
		} catch (ConnectionException | TransportException e) {

		}

		String command = "cd " + PATH_SERVER + "/" + submission.getId() + "_" + submission.getLanguage().getName()
				+ " && ";
		String runError = "runtime_error_" + submission.getId() + ".txt";
		String runOutput = "output_" + submission.getId() + "_" + testCase.getId() + ".txt";

		// TODO : add memory limit
		switch (submission.getLanguage().getCompilerName()) {
		case "Java 8":
			command += "cat " + testCase.getId() + " | timeout 3s java " + fileName + " 2> " + runError + " > "
					+ runOutput + "";
			break;
		case "C++11 (gcc 5.4.0)":
			command += "cat " + testCase.getId() + " | timeout 2 ./" + fileName + "_exec_" + uniqueId + " 2> "
					+ runError + " > " + runOutput + "";
			break;
		case "Python 2.7":
			command += "cat " + testCase.getId() + " | timeout 10 python " + fileName + ".py 2> " + runError + " > "
					+ runOutput + "";
			break;
		case "C# (mono 4.2.1)":
			command += "cat " + testCase.getId() + " | timeout 3 ./" + fileName + "_exec_" + uniqueId + " 2> "
					+ runError + " > " + runOutput + "";
			break;
		default:
			break;
		}

		command += " && cat " + runOutput;

		try {

			Command cmd = session.exec(command);
			String output = IOUtils.readFully(cmd.getInputStream()).toString();

			Problem problem = submission.getProblem();
			int isRight = validateResult(testCase.getOutput(), output);
			int totalTestCasesForProblem = problem.getTestCases().size();

			int givenTestCases = 0;
			for (TestCases tc : problem.getTestCases()) {
				if (tc.isShown()) {
					givenTestCases++;
				}
			}

			double score = isRight == 1
					? (submission.getProblem().getMaxScore() / (totalTestCasesForProblem - givenTestCases))
					: 0;

			Scoring sc = new Scoring(submission, testCase, score, isRight);
			scoringRepository.save(sc);

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
				} else {
					submission.setResult(resultRepository.findByName("Incorrect"));
				}
				submission.setScore(totalScore);
				submissionsRepository.save(submission);

				List<Leaderboard> highestSubmissionOnLeaderboard = leaderboardRepository
						.findHighestScoreByUserByProblem(submission.getUsers(), submission.getProblem());

				Leaderboard newLeaderboard = new Leaderboard(totalScore, submission.getUsers(),
						submission.getProblem());

				if (highestSubmissionOnLeaderboard.size() > 0) {

					Leaderboard maxScoreSubmission = highestSubmissionOnLeaderboard.get(0);

					// ugly, can't figure out how to load an object using only the id from the
					// previous query
					Optional<Leaderboard> currentLeaderboard = leaderboardRepository
							.findById(maxScoreSubmission.getId());

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

			cmd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private int validateResult(String tcOutput, String output) {
		if (tcOutput.trim().equals(output.trim())) {
			return 1;
		} else if (output.trim().equals("")) {
			return -1;
		}
		return 0;
	}

	public void createFile(String text, String fileName) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(PATH_SPRING + "/" + fileName, "UTF-8");
			writer.print(text);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void scp(String src, String dest) {

		try {
			// System.out.println("SRC: " + src);
			// System.out.println("DEST: " + dest);
			ssh.newSCPFileTransfer().upload(new FileSystemFile(src), dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
