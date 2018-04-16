package pt.codeflex.controllers;

import pt.codeflex.evaluatesubmissions.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;

import org.springframework.context.ApplicationContext;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.xfer.FileSystemFile;

@Controller
public class CompilerController {

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext applicationContext;

	@GetMapping("/compiler")
	public String compiler() {
		return "compiler";
	}

	@GetMapping("/ssh")
	public String ssh() throws IOException {
		EvaluateSubmissions.connect();
		long inicial = System.currentTimeMillis();

		for (int i = 0; i < 100 ; i++) {

			EvaluateSubmissions evaluateSubmissions = new EvaluateSubmissions();
			
			evaluateSubmissions.compileAndRun("JAVA", "import java.io.*;\r\n" + "import java.util.*;\r\n"
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
			
//			EvaluateSubmissions evaluateSubmissions = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions);
//
//			EvaluateSubmissions evaluateSubmissions2 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions2);
//
//			EvaluateSubmissions evaluateSubmissions3 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions3);
//
//			EvaluateSubmissions evaluateSubmissions4 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions4);
//
//			EvaluateSubmissions evaluateSubmissions5 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions5);
//
//			EvaluateSubmissions evaluateSubmissions6 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions6);
//
//			EvaluateSubmissions evaluateSubmissions7 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions7);
//
//			EvaluateSubmissions evaluateSubmissions8 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions8);
//
//			EvaluateSubmissions evaluateSubmissions9 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions9);
//
//			EvaluateSubmissions evaluateSubmissions10 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions10);

	
			// EvaluateSubmissions.compileAndRun("C++11", "#include <algorithm>\r\n" +
			// "#include <cmath>\r\n"
			// + "#include <bitset>\r\n" + "#include <cstdio>\r\n" + "#include
			// <cstdlib>\r\n"
			// + "#include <cstring>\r\n" + "#include <iostream>\r\n" + "#include
			// <iomanip>\r\n"
			// + "#include <map>\r\n" + "#include <set>\r\n" + "#include <sstream>\r\n" +
			// "#include <string>\r\n"
			// + "#include <unordered_map>\r\n" + "#include <unordered_set>\r\n" + "#include
			// <vector>\r\n"
			// + "#include <queue>\r\n" + "\r\n" + "using namespace std;\r\n" + "\r\n"
			// + "#define all(x) x.begin(), x.end()\r\n" + "#define forn(i, n) for(int i =
			// 0; i < (n); ++i)\r\n"
			// + "#define debug(x) std::cerr << \"DEBUG: \" << #x << \" = \" << (x) <<
			// std::endl\r\n"
			// + "#define mp make_pair\r\n" + "#define pb push_back\r\n"
			// + "#define PATH \"C:\\\\Users\\\\Valentin\\\\Desktop\\\\\"\r\n" + "\r\n"
			// + "template<class T> inline int sz(const T& x) { return (int) x.size();
			// }\r\n"
			// + "template<class T> inline void mx(T& x, const T& y) { x = std::max(x, y);
			// }\r\n"
			// + "template<class T> inline void mn(T& x, const T& y) { x = std::min(x, y);
			// }\r\n" + "\r\n"
			// + "// SOLUTION BEGINS HERE\r\n" + "\r\n" + "void run() {\r\n" + " int n;\r\n"
			// + " cin >> n;\r\n"
			// + " vector<pair<int, int>> a(n);\r\n" + " forn (i, n) {\r\n" + " cin >>
			// a[i].first;\r\n"
			// + " a[i].second = i;\r\n" + " }\r\n" + " sort(all(a));\r\n" + " int ans =
			// n;\r\n"
			// + " forn (i, n - 1) {\r\n" + " if (a[i].first == a[i + 1].first) {\r\n"
			// + " mn(ans, a[i + 1].second - a[i].second);\r\n" + " }\r\n" + " }\r\n"
			// + " if (ans == n) {\r\n" + " ans = -1;\r\n" + " }\r\n" + " cout << ans <<
			// endl;\r\n" + "}\r\n"
			// + "\r\n" + "\r\n" + "// SOLUTION ENDS HERE\r\n" + "\r\n" + "int main() {\r\n"
			// + " // freopen(PATH\"in.txt\", \"r\", stdin);\r\n" + "
			// std::ios_base::sync_with_stdio(false);\r\n"
			// + " std::cin.tie(nullptr);\r\n" + " run();\r\n" + " std::cout.flush();\r\n" +
			// " return 0;\r\n"
			// + "}", "input.txt", "");
			//
			// EvaluateSubmissions.compileAndRun("PYTHON",
			// "#!/bin/python\r\n" + "\r\n" + "import sys\r\n" + "\r\n" + "\r\n"
			// + "n = int(raw_input().strip())\r\n" + "A =
			// map(int,raw_input().strip().split(' '))\r\n"
			// + "r=n+1\r\n" + "for i in range(n):\r\n" + " for j in range(i+1,n):\r\n"
			// + " if A[j]==A[i]:\r\n" + " r=min(j-i,r)\r\n" + "if r<=n:\r\n"
			// + " print r\r\n" + "else:\r\n" + " print -1",
			// "input.txt", "");
			//
			// EvaluateSubmissions.compileAndRun("C#",
			// "using System;\r\n" + "using System.Collections.Generic;\r\n" + "using
			// System.IO;\r\n"
			// + "using System.Linq;\r\n" + "class Solution {\r\n" + "\r\n"
			// + " static void Main(String[] args) {\r\n"
			// + " int n = Convert.ToInt32(Console.ReadLine());\r\n"
			// + " string[] A_temp = Console.ReadLine().Split(' ');\r\n"
			// + " int[] A = Array.ConvertAll(A_temp,Int32.Parse);\r\n" + " \r\n"
			// + " int min = int.MaxValue;\r\n" + " for(int i=0;i<n-1;i++){\r\n"
			// + " for(int j=i+1;j<n;j++){\r\n" + " if(A[i]==A[j]){\r\n"
			// + " int d = j-i;\r\n" + " if(d<min)min=d;\r\n"
			// + " }\r\n" + " }\r\n" + " }\r\n"
			// + " if(min==int.MaxValue) Console.WriteLine(-1);\r\n"
			// + " else Console.WriteLine(min);\r\n" + " }\r\n" + "}",
			// "input.txt", "");
		}
		System.out.println("TEMPO DE PROCESSAMENTO : " + (System.currentTimeMillis() - inicial));
		return "";
	}

}
