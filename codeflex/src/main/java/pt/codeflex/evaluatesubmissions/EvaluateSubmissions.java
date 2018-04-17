package pt.codeflex.evaluatesubmissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
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
import pt.codeflex.models.Users;
import pt.codeflex.repositories.SubmissionsRepository;
import pt.codeflex.repositories.UsersRepository;

@Component
@Scope("prototype")
public class EvaluateSubmissions implements Runnable {

		@Autowired
	private DatabaseController databaseController;

	@Autowired
	private SubmissionsRepository submissionsRepository;

	private Queue<Submissions> queue = new ArrayDeque<>();
	private static final String SUBMISSIONS_FILE_DIR = System.getProperty("user.home") + File.separator + "Submissions";
	private static final String SUBMISSIONS_SERVER_DIR = "/home/mbrito/Desktop/Submissions";
	private static long uniqueId = 1000;

	public void getSubmissions(long id) {
		Iterable<Users> u = databaseController.getAllUsers();
		
		for (Users us : u) {
			if (us.getId() == id) {
				List<Submissions> submissions = us.getSubmissions();
				for (Submissions s : submissions) {
					System.out.println("SUBMISSAO");
					System.out.println(s.getCode());
					System.out.println(s.getLanguage());
					System.out.println(s.getDate());
					System.out.println();
					
					queue.add(s);
				}
				System.out.println("QUEUE");
				while (!queue.isEmpty()) {
					Submissions s = queue.poll();
					System.out.println(s.getLanguage());
					System.out.println(s.getCode());
				}
			}
		}

	}

	public static SSHClient ssh = null;

	public static void connect() {
		ssh = new SSHClient();
		ssh.addHostKeyVerifier("33:02:cb:3b:13:b1:bd:fa:66:ff:29:96:ea:ff:dc:78");
		try {
			ssh.connect("192.168.1.55");
			ssh.authPassword("mbrito", "gomas");
			Session session = ssh.startSession();
			Command cmd = session.exec("ls");
			System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR CONNETING");
			e.printStackTrace();
		}

	}

	public void compileSubmission(String language, String code, String input, String output) {
		uniqueId++;
		Session session = null;
		try {
			session = ssh.startSession();
		} catch (ConnectionException | TransportException e2) {
			e2.printStackTrace();
		}

		String fileName = "Solution";
		String suffix = "";
		String compileError = "error_" + uniqueId + ".txt";
		String compileOutput = "output_" + uniqueId + ".txt";
		String command = "cd Desktop/Submissions/" + uniqueId + "_" + language + " && ";
		// TODO : add memory limit

		switch (language) {
		case "JAVA":
			command += "javac " + fileName + ".java 2> " + compileError; // && disown
			suffix = ".java";
			break;
		case "C++11":
			command += "g++ -std=c++11 -o " + fileName + "_exec_" + uniqueId + ".cpp " + fileName + " 2> "
					+ compileError;
			suffix = ".cpp";
			break;
		case "PYTHON":
			command += "cat " + input + " | timeout 10 python " + fileName + ".py 2> " + compileError + " > "
					+ compileOutput + "";
			suffix = ".py";
			break;
		case "C#":
			command += "mcs -out:" + fileName + "_exec_" + uniqueId + " " + fileName + ".cs 2> " + compileError;
			suffix = ".cs";
			break;
		default:
			System.out.println("Language not found");
			break;
		}

		// Create file from the code submitted
		createFile(code, "Solution");

		// Create file from the input submitted
		createFile(input, "input.txt");

		Command cmd;
		try {
			cmd = session.exec("mkdir Desktop/Submissions/" + uniqueId + "_" + language);
			cmd.close();
			session.close();
		} catch (ConnectionException | TransportException e1) {
			e1.printStackTrace();
		}

		// Send compilation file to the server
		scp(SUBMISSIONS_FILE_DIR + "/" + fileName,
				SUBMISSIONS_SERVER_DIR + "/" + uniqueId + "_" + language + "/Solution" + suffix);

		// Send input file to the server

		try {
			session = ssh.startSession();
			System.out.println("COMANDO DE EXEC: " + command);
			cmd = session.exec(command);
			// System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());

			session.close();
			cmd.close();
			for (int i = 0; i < 25; i++) {
				System.out.println("Running tests after compiling!");
				runSubmission(language, fileName, String.valueOf(uniqueId), input, i);
			}

		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void runSubmission(String language, String fileName, String id, String input, int count) {
		Session session = null;
		try {
			session = ssh.startSession();
		} catch (ConnectionException | TransportException e) {
			e.printStackTrace();
		}

		String command = "cd " + SUBMISSIONS_SERVER_DIR + "/" + id + "_" + language + " && ";
		Command cmd;

		String compileError = "error_" + uniqueId + ".txt";
		String compileOutput = "output_" + uniqueId + "_" + count + ".txt";

		// TODO : add memory limit
		switch (language) {
		case "JAVA":
			command += "cat " + input + " | timeout 3s java " + fileName + " > " + compileOutput + ""; // && disown
			break;
		case "C++11":
			command += "cat " + input + " | timeout 2 ./" + fileName + "_exec_" + uniqueId + " 2> " + compileError
					+ " > " + compileOutput + "";
			break;
		case "PYTHON":
			command += "cat " + input + " | timeout 10 python " + fileName + ".py 2> " + compileError + " > "
					+ compileOutput + "";
			break;
		case "C#":
			command += "cat " + input + " | mono " + fileName + "_exec_" + uniqueId + " 2> " + compileError + " > "
					+ compileOutput + "";
			break;
		default:
			System.out.println("Language not found");
			break;
		}

		try {
			cmd = session.exec(command);
			session.close();
			cmd.close();
		} catch (ConnectionException | TransportException e) {
			e.printStackTrace();
		}

	}

	public static void getFinishedJobs() { // TODO : review
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

	public static void createFile(String code, String fileName) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(SUBMISSIONS_FILE_DIR + "/" + fileName, "UTF-8");
			writer.print(code);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void scp(String src, String dest) {

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
		System.out.println("THREAD!!!!! ");
		compileSubmission("JAVA", "import java.io.*;\r\n" + "import java.util.*;\r\n" + "import java.text.*;\r\n"
				+ "import java.math.*;\r\n" + "import java.util.regex.*;\r\n" + "\r\n" + "public class Solution {\r\n"
				+ "\r\n" + "	public static void main(String[] args) {\r\n"
				+ "		Scanner in = new Scanner(System.in);\r\n" + "		int n = in.nextInt();\r\n"
				+ "		int scores[] = new int[n];\r\n" + "		for (int i = 0; i < n; i++) {\r\n"
				+ "			scores[i] = in.nextInt();\r\n" + "		}\r\n" + "		minimumDistances(n, scores);\r\n"
				+ "	}\r\n" + "\r\n" + "	static void minimumDistances(int n, int array[]) {\r\n"
				+ "		int min = Integer.MAX_VALUE;\r\n" + "		for (int i = 0; i < n-1; i++) {\r\n"
				+ "			for(int j = i+1; j<n; j++) {\r\n" + "				if(array[i] == array[j]){\r\n"
				+ "					min = Math.min(min,  Math.abs(i-j));\r\n" + "				}\r\n"
				+ "			}\r\n" + "		}\r\n" + "		if(min==Integer.MAX_VALUE) {\r\n"
				+ "			min = -1;\r\n" + "		}\r\n" + "		System.out.println(min);\r\n" + "	}\r\n" + "\r\n"
				+ "\r\n" + "}", "../input.txt", "nothing");
	}

}
