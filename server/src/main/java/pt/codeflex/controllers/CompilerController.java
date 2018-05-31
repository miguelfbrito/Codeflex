package pt.codeflex.controllers;

import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.Users;
import pt.codeflex.evaluatesubmissions.*;
import pt.codeflex.models.SubmitSubmission;
import pt.codeflex.repositories.ProblemRepository;
import pt.codeflex.repositories.SubmissionsRepository;
import pt.codeflex.repositories.UsersRepository;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class CompilerController {

	private final String host1 = "192.168.1.55"; // "10.214.104.240";  "192.168.1.55"
	private final String host2 = "192.168.1.65"; // "10.214.104.235"; "192.168.1.65"

	private Queue<Submissions> queue = new ArrayDeque<>();

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SubmissionsRepository submissionsRepository;

	@Autowired
	private ProblemRepository problemRepository;

	@Autowired
	private UsersRepository usersRepository;

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
		// EvaluateSubmissions evaluateSubmissions5 =
		// applicationContext.getBean(EvaluateSubmissions.class);
		// evaluateSubmissions5.setHost(host1);
		// evaluateSubmissions5.connect(host1);
		// taskExecutor.execute(evaluateSubmissions5);

		System.out.println("TEMPO DE PROCESSAMENTO : " + (System.currentTimeMillis() - inicial));
		return "";
	}

	@PostMapping("/submission")
	public void property(@RequestBody SubmitSubmission submitSubmission) {
		System.out.println(submitSubmission.getCode());
		System.out.println(submitSubmission.getLanguage());
		System.out.println(submitSubmission.getProblem().getName());
		System.out.println(submitSubmission.getUsers().getId());

		Problem problem = problemRepository.findByName(submitSubmission.getProblem().getName().replaceAll("-", " "));
		Optional<Users> u = usersRepository.findById((submitSubmission.getUsers().getId()));
System.out.println("DATA");
		System.out.println(problem.toString());
		System.out.println(u.get().toString());
		if (problem != null && u.isPresent()) {
			System.out.println("HERE!");
			Submissions s = new Submissions(problem, submitSubmission.getLanguage(), submitSubmission.getCode());
			Users user = u.get();
			user.getSubmissions().add(s);
			submissionsRepository.save(s);
			usersRepository.save(user);
			
			try {
				System.out.println("Submit for compilation and execution!");
				ssh();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
