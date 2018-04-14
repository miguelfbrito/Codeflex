package pt.codeflex.evaluatesubmissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.xfer.FileSystemFile;

public class EvaluateSubmissions {

	private static final String SUBMISSIONS_FILE_DIR = System.getProperty("user.home") + File.separator + "Submissions";
	private static final String SUBMISSIONS_SERVER = "/home/mbrito/Desktop/Submissions";
	private long uniqueId = 1000;

	public EvaluateSubmissions() {
		this.uniqueId++;
	}
	public SSHClient ssh = null;

	public void connect() {

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

	public void createFile(String code, long uniqueId) {
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
		String compileError = "cmp_error_" + fileName + "_" + uniqueId + ".txt";

		String command = "cd Desktop/Submissions && ";
		switch (language) {
		case "JAVA":
			command += "javac " + fileName + ".java 2> " + compileError + " && cat " + input + " | timeout 3s java "
					+ fileName + " > output_" + uniqueId + ".txt";
			break;
		case "C++11":
			command += "g++ -std=c++11 -o " + fileName + "_cpp_exec " + fileName + " && cat " + input + " | ./"
					+ fileName + "_cpp_exec";
			break;

		default:
			break;
		}

		try {
			System.out.println("COMANDO DE EXEC: " + command);
			Command cmd = session.exec(command);
			System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
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

	public void scp(String language, String src, String dest) {
		// final String src = System.getProperty("user.home") + File.separator +
		String suffix = "";
		switch (language) {
		case "JAVA":
			suffix = ".java";
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

}
