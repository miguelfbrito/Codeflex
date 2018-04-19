package pt.codeflex.evaluatesubmissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import pt.codeflex.controllers.api.DatabaseController;
import pt.codeflex.models.Submissions;
import pt.codeflex.models.TestCases;
import pt.codeflex.models.Users;
import pt.codeflex.repositories.SubmissionsRepository;
import pt.codeflex.repositories.UsersRepository;

@Component
@Scope("prototype")
public class EvaluateSubmissions implements Runnable {

	@Autowired
	private UsersRepository usersRepository;

	private Queue<Submissions> queue = new ArrayDeque<>();
	private final String PATH_SPRING = System.getProperty("user.home") + File.separator + "Submissions";
	private final String PATH_SERVER = "/home/mbrito/Desktop/Submissions";
	private long uniqueId;

	public void getSubmissions(long id) {
		Optional<Users> u = usersRepository.findById(id);

		if (u.isPresent()) {
			connect();
			Users us = u.get();
			List<Submissions> submissions = us.getSubmissions();

			for (Submissions s : submissions) {
				queue.add(s);
			}

			System.out.println("QUEUE");
			while (!queue.isEmpty()) {
				Submissions s = queue.poll();
				compileSubmission(s);
			}

		}

	}

	public SSHClient ssh = null;

	public void connect() {
		ssh = new SSHClient();
		ssh.addHostKeyVerifier("33:02:cb:3b:13:b1:bd:fa:66:ff:29:96:ea:ff:dc:78");
		try {
			ssh.loadKnownHosts();
			ssh.connect("10.214.104.233");
			ssh.authPassword("mbrito", "gomas");
			Session session = ssh.startSession();
			Command cmd = session.exec("ls");
			System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());

		} catch (IOException e) {
			System.out.println("Error connecting!");
			e.printStackTrace();
		}

	}

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
		String compileError = "compiler_error_" + uniqueId + ".txt";
		String command = "cd Desktop/Submissions/" + uniqueId + "_" + submission.getLanguage() + " && ";
		// TODO : add memory limit

		switch (submission.getLanguage()) {
		case "JAVA":
			command += "javac " + fileName + ".java 2> " + compileError; // && disown
			suffix = ".java";
			break;
		case "C++11":
			command += "g++ -std=c++11 -o " + fileName + "_exec_" + uniqueId + " " + fileName + ".cpp 2> "
					+ compileError;
			suffix = ".cpp";
			break;
		// case "PYTHON":
		// command += "cat " + input + " | timeout 10 python " + fileName + ".py 2> " +
		// compileError + " > "
		// + compileOutput + "";
		// suffix = ".py";
		// break;
		case "C#":
			command += "mcs -out:" + fileName + "_exec_" + uniqueId + " " + fileName + ".cs 2> " + compileError;
			suffix = ".cs";
			break;
		default:
			System.out.println("Language not found");
			break;
		}

		Command cmd;
		try {
			cmd = session.exec("mkdir Desktop/Submissions/" + uniqueId + "_" + submission.getLanguage());
			cmd.close();

		} catch (ConnectionException | TransportException e1) {
			e1.printStackTrace();
		}

		// Create and send the code to the server
		createFile(new String(Base64.getDecoder().decode(submission.getCode())), "Solution");
		scp(PATH_SPRING + "/" + fileName,
				PATH_SERVER + "/" + uniqueId + "_" + submission.getLanguage() + "/Solution" + suffix);

		try {
			session = ssh.startSession();
			System.out.println("COMANDO DE EXEC: " + command);
			cmd = session.exec(command);

			cmd.close();

			List<TestCases> testCases = submission.getProblem().getTestCases();
			for (TestCases tc : testCases) {

				String tcFileName = String.valueOf(tc.getId());
				createFile(tc.getInput(), tcFileName);
				scp(PATH_SPRING + "/" + tcFileName,
						PATH_SERVER + "/" + submission.getId() + "_" + submission.getLanguage() + "/");

				System.out.println("Running tests case " + tc.getId() + " for " + submission.getLanguage());
				runSubmission(submission.getLanguage(), fileName, tcFileName, submission.getId(), tc.getId());
			}
		} catch (ConnectionException | TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void runSubmission(String language, String fileName, String input, long submissionId, long testCaseId) {
		Session session = null;
		try {
			session = ssh.startSession();
		} catch (ConnectionException | TransportException e) {
			e.printStackTrace();
		}

		String command = "cd " + PATH_SERVER + "/" + submissionId + "_" + language + " && ";
		String runError = "error_" + submissionId + ".txt";
		String runOutput = "output_" + submissionId + "_" + testCaseId + ".txt";

		// TODO : add memory limit
		switch (language) {
		case "JAVA":
			command += "cat " + input + " | timeout 3s java " + fileName + " 2> " + runError + " > " + runOutput + ""; // &&
																														// disown
			break;
		case "C++11":
			command += "cat " + input + " | timeout 2 ./" + fileName + "_exec_" + uniqueId + " 2> " + runError + " > "
					+ runOutput + "";
			break;
		case "PYTHON":
			command += "cat " + input + " | timeout 10 python " + fileName + ".py 2> " + runError + " > " + runOutput
					+ "";
			break;
		case "C#":
			command += "cat " + input + " | ./" + fileName + "_exec_" + uniqueId + " 2> " + runError + " > " + runOutput
					+ "";
			break;
		default:
			System.out.println("Language not found");
			break;
		}

		try {
			Command cmd = session.exec(command);

			cmd.close();
		} catch (ConnectionException | TransportException e) {
			e.printStackTrace();
		}

	}

	public void getFinishedJobs() { // TODO : review
		Session session = null;
		String output = "";
		try {
			session = ssh.startSession();
		} catch (ConnectionException | TransportException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String command = "cd Desktop/Submissions && cat output_*";
		try {
			Command cmd = session.exec(command);
			output = IOUtils.readFully(cmd.getInputStream()).toString();
		} catch (ConnectionException | TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(output);

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
			System.out.println("SRC: " + src);
			System.out.println("DEST: " + dest);
			ssh.newSCPFileTransfer().upload(new FileSystemFile(src), dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Thread starting!");

		System.out.println("Connection established!");
		getSubmissions(1);

	}

}
