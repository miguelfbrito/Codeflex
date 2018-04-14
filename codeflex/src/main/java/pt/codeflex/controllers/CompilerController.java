package pt.codeflex.controllers;

import pt.codeflex.evaluatesubmissions.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;

import org.mockito.internal.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.xfer.FileSystemFile;

@Controller
public class CompilerController {

	
	@GetMapping("/compiler")
	public String compiler() {
		return "compiler";
	}

	public EvaluateSubmissions evaluateSubmissions;

	@GetMapping("/ssh")
	public String ssh() throws IOException {
		evaluateSubmissions = new EvaluateSubmissions();
		evaluateSubmissions.connect();
		evaluateSubmissions.compileAndRun("JAVA", "import java.io.*;\r\n" + 
				"import java.util.*;\r\n" + 
				"import java.text.*;\r\n" + 
				"import java.math.*;\r\n" + 
				"import java.util.regex.*;\r\n" + 
				"\r\n" + 
				"public class Solution {\r\n" + 
				"\r\n" + 
				"	public static void main(String[] args) {\r\n" + 
				"		Scanner in = new Scanner(System.in);\r\n" + 
				"		int n = in.nextInt();\r\n" + 
				"		int scores[] = new int[n];\r\n" + 
				"		for (int i = 0; i < n; i++) {\r\n" + 
				"			scores[i] = in.nextInt();\r\n" + 
				"		}\r\n" + 
				"		minimumDistances(n, scores);\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	static void minimumDistances(int n, int array[]) {\r\n" + 
				"		int min = Integer.MAX_VALUE;\r\n" + 
				"		for (int i = 0; i < n-1; i++) {\r\n" + 
				"			for(int j = i+1; j<n; j++) {\r\n" + 
				"				if(array[i] == array[j]){\r\n" + 
				"					min = Math.min(min,  Math.abs(i-j));\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"		if(min==Integer.MAX_VALUE) {\r\n" + 
				"			min = -1;\r\n" + 
				"		}\r\n" + 
				"		System.out.println(min);\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"}", "input.txt", "tochange");
		
		return "";
	}

}
