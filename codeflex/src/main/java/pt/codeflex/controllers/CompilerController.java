package pt.codeflex.controllers;

import pt.codeflex.evaluatesubmissions.*;
import pt.codeflex.models.Submissions;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompilerController {

	private final String host1 = "192.168.1.55"; // "10.214.104.240";
	private final String host2 = "192.168.1.65"; //"10.214.104.235";
	
	private Queue<Submissions> queue = new ArrayDeque<>();
	
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
		long inicial = System.currentTimeMillis();
		EvaluateSubmissions evaluateSubmissions1 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions1.setHost(host1);
		evaluateSubmissions1.connect(host1);
		taskExecutor.execute(evaluateSubmissions1);

		EvaluateSubmissions evaluateSubmissions2 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions2.setHost(host2);
		evaluateSubmissions2.connect(host2);
		taskExecutor.execute(evaluateSubmissions2);

		EvaluateSubmissions evaluateSubmissions3 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions3.setHost(host1);
		evaluateSubmissions3.connect(host1);
		taskExecutor.execute(evaluateSubmissions3);

		EvaluateSubmissions evaluateSubmissions4 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions4.setHost(host2);
		evaluateSubmissions4.connect(host2);
		taskExecutor.execute(evaluateSubmissions4);
//
//		EvaluateSubmissions evaluateSubmissions5 = applicationContext.getBean(EvaluateSubmissions.class);
//		evaluateSubmissions5.setHost(host1);
//		evaluateSubmissions5.connect(host1);
//		taskExecutor.execute(evaluateSubmissions5);

		System.out.println("TEMPO DE PROCESSAMENTO : " + (System.currentTimeMillis() - inicial));
		return "";
	}

	@PostMapping("/post")
	public String postTest() {
		// evaluateSubmissions.createFile(text, fileName);
		return "";
	}
}
