package pt.codeflex.controllers;

import pt.codeflex.evaluatesubmissions.*;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompilerController {

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private EvaluateSubmissions evaluateSubmissions;

	@GetMapping("/compiler")
	public String compiler() {
		return "compiler";
	}

	@GetMapping("/ssh")
	public String ssh() throws IOException {
		long inicial = System.currentTimeMillis();
		EvaluateSubmissions evaluateSubmissions1 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions1.connect();
		taskExecutor.execute(evaluateSubmissions1);

		EvaluateSubmissions evaluateSubmissions2 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions2.connect();
		taskExecutor.execute(evaluateSubmissions2);

		EvaluateSubmissions evaluateSubmissions3 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions3.connect();
		taskExecutor.execute(evaluateSubmissions3);

		EvaluateSubmissions evaluateSubmissions4 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions4.connect();
		taskExecutor.execute(evaluateSubmissions4);

		EvaluateSubmissions evaluateSubmissions5 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions5.connect();
		taskExecutor.execute(evaluateSubmissions5);

		System.out.println("TEMPO DE PROCESSAMENTO : " + (System.currentTimeMillis() - inicial));
		return "";
	}

	@PostMapping("/post")
	public String postTest() {
		// evaluateSubmissions.createFile(text, fileName);
		return "";
	}
}
