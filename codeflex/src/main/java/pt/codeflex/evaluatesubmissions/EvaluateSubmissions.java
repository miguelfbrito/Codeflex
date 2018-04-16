package pt.codeflex.evaluatesubmissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.xfer.FileSystemFile;
import pt.codeflex.controllers.api.DatabaseController;
import pt.codeflex.models.Submissions;

@Component
@Scope("prototype")
public class EvaluateSubmissions implements Runnable{

	private static final String SUBMISSIONS_FILE_DIR = System.getProperty("user.home") + File.separator + "Submissions";
	private static final String SUBMISSIONS_SERVER = "/home/mbrito/Desktop/Submissions";
	private static long uniqueId = 1000;

	private static Queue<Submissions> submissions;

	@Autowired
	private DatabaseController databaseController;

	public EvaluateSubmissions() {
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

	public void compileAndRun(String language, String code, String input, String output) {

		String fileName = "Solution";
		createFile(code, uniqueId);
		scp(language, SUBMISSIONS_FILE_DIR + "/" + fileName, SUBMISSIONS_SERVER);

		Session session = null;

		try {
			session = ssh.startSession();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		uniqueId = uniqueId + 1;
		String compileError = "error_" + uniqueId + ".txt";
		String compileOutput = "output_" + uniqueId + ".txt";
		String command = "cd Desktop/Submissions && ";
		// TODO : add memory limit
		switch (language) {
		case "JAVA":
			command += "javac " + fileName + ".java 2> " + compileError + " && cat " + input + " | timeout 3s java "
					+ fileName + " > " + compileOutput + ""; // && disown
			break;
		case "C++11":
			command += "nohup g++ -std=c++11 -o " + fileName + "_exec_" + uniqueId + ".cpp " + fileName + " 2> "
					+ compileError + " && nohup cat " + input + " | timeout 2 ./" + fileName + "_exec_" + uniqueId + " 2> "
					+ compileError + " > " + compileOutput + "";
			break;
		case "PYTHON":
			command += "nohup cat " + input + " | nohup timeout 10 python " + fileName + ".py 2> " + compileError + " > "
					+ compileOutput + "";
			break;
		case "C#":
			command += "nohup mcs -out:" + fileName + "_exec_" + uniqueId + " " + fileName + ".cs 2> " + compileError
					+ " && cat " + input + " | nohup mono " + fileName + "_exec_" + uniqueId + " 2> " + compileError + " > "
					+ compileOutput + "";
			break;
		default:
			System.out.println("Language not found");
			break;
		}

		try {
			System.out.println("COMANDO DE EXEC: " + command);
			Command cmd = session.exec(command);
			System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
			cmd.close();
			session.close();
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

	public static void createFile(String code, long uniqueId) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(SUBMISSIONS_FILE_DIR + "/Solution", "UTF-8");
			writer.print(code);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void scp(String language, String src, String dest) {
		// final String src = System.getProperty("user.home") + File.separator +
		String suffix = "";
		switch (language) {
		case "JAVA":
			suffix = ".java";
			break;
		case "C++11":
			suffix = ".cpp";
			break;
		case "PYTHON":
			suffix = ".py";
			break;
		case "C#":
			suffix = ".cs";
			break;
		default:
			break;
		}
		try {
			System.out.println("SRC: " + src);
			System.out.println("DEST: " + dest);
			ssh.newSCPFileTransfer().upload(new FileSystemFile(src), dest + "/Solution" + suffix);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("THREAD!!!!! ");
		compileAndRun("JAVA", "import java.io.*;\r\n" + "import java.util.*;\r\n"
				+ "import java.text.*;\r\n" + "import java.math.*;\r\n" + "import java.util.regex.*;\r\n" + "\r\n"
				+ "public class Solution {\r\n" + "\r\n" + "	public static void main(String[] args) {\r\n"
				+ "		Scanner in = new Scanner(System.in);\r\n" + "		int n = in.nextInt();\r\n"
				+ "		int scores[] = new int[n];\r\n" + "		for (int i = 0; i < n; i++) {\r\n"
				+ "			scores[i] = in.nextInt();\r\n" + "		}\r\n"
				+ "		minimumDistances(n, scores);\r\n" + "	}\r\n" + "\r\n"
				+ "	static void minimumDistances(int n, int array[]) {\r\n"
				+ "		int min = Integer.MAX_VALUE;\r\n" + "		for (int i = 0; i < n-1; i++) {\r\n"
				+ "			for(int j = i+1; j<n; j++) {\r\n" + "				if(array[i] == array[j]){\r\n"
				+ "					min = Math.min(min,  Math.abs(i-j));\r\n" + "				}\r\n"
				+ "			}\r\n" + "		}\r\n" + "		if(min==Integer.MAX_VALUE) {\r\n"
				+ "			min = -1;\r\n" + "		}\r\n" + "		System.out.println(min);\r\n" + "	}\r\n"
				+ "\r\n" + "\r\n" + "}", "input.txt", "nothing");
	}

}
