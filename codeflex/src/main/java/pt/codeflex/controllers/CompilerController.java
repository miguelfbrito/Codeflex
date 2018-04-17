package pt.codeflex.controllers;

import pt.codeflex.evaluatesubmissions.*;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



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
		EvaluateSubmissions.connect();
		long inicial = System.currentTimeMillis();
	    evaluateSubmissions.getSubmissions(1);
//		for (int i = 0; i < 100 ; i++) {
//			
//			EvaluateSubmissions evaluateSubmissions1 = applicationContext.getBean(EvaluateSubmissions.class);
//			taskExecutor.execute(evaluateSubmissions1);
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
//			
//
//
//		}
		System.out.println("TEMPO DE PROCESSAMENTO : " + (System.currentTimeMillis() - inicial));
		return "";
	}

}
