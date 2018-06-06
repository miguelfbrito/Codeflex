package pt.codeflex.controllers;

import pt.codeflex.databasemodels.Language;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Scoring;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.Users;
import pt.codeflex.evaluatesubmissions.*;
import pt.codeflex.models.SubmitSubmission;
import pt.codeflex.repositories.LanguageRepository;
import pt.codeflex.repositories.ProblemRepository;
import pt.codeflex.repositories.ScoringRepository;
import pt.codeflex.repositories.SubmissionsRepository;
import pt.codeflex.repositories.UsersRepository;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.transaction.Transactional;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CompilerController {

	private final String host1 = "192.168.1.55";// "192.168.43.250"; // "10.214.104.240"; "192.168.1.55"
	private final String host2 = "192.168.1.65"; //"192.168.43.155"; // "10.214.104.235"; "192.168.1.65"

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

	@Autowired
	private ScoringRepository scoringRepository;
	
	@Autowired
	private LanguageRepository languageRepository;

	@Autowired 
	private EvaluateSubmissions evaluateSubmissions;
	@GetMapping("/compiler")
	public String compiler() {
		return "compiler";
	}

	@GetMapping("/ssh")
	public void startThreads() throws IOException, InterruptedException {
		long inicial = System.currentTimeMillis();
		
		
		// Adds all the submissions into a volatile queue. The queue is then distributed to the threads
		evaluateSubmissions.connect(host1);
		evaluateSubmissions.getSubmissions();
		
		EvaluateSubmissions evaluateSubmissions1 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions1.connect(host1);
		taskExecutor.execute(evaluateSubmissions1);

		EvaluateSubmissions evaluateSubmissions2 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions2.connect(host2);
		taskExecutor.execute(evaluateSubmissions2);

//		EvaluateSubmissions evaluateSubmissions3 = applicationContext.getBean(EvaluateSubmissions.class);
//		evaluateSubmissions3.setHost(host1);
//		evaluateSubmissions3.connect(host1);
//		taskExecutor.execute(evaluateSubmissions3);
//
//		EvaluateSubmissions evaluateSubmissions4 = applicationContext.getBean(EvaluateSubmissions.class);
//		evaluateSubmissions4.setHost(host2);
//		evaluateSubmissions4.connect(host2);
//		taskExecutor.execute(evaluateSubmissions4);

		System.out.println("TEMPO DE PROCESSAMENTO : " + (System.currentTimeMillis() - inicial));
	}

	@Transactional
	@PostMapping("/submission")
	public Submissions submit(@RequestBody SubmitSubmission submitSubmission) {

		Problem problem = problemRepository.findByName(submitSubmission.getProblem().getName().replaceAll("-", " "));
		Optional<Users> u = usersRepository.findById(submitSubmission.getUsers().getId());
		Language language = languageRepository.findByName(submitSubmission.getLanguage().getName());

		Submissions submission = new Submissions();
		if (problem != null && language != null && u.isPresent()) {
			Users user = u.get();
			submission = new Submissions(problem, language, submitSubmission.getCode(), user);
			submissionsRepository.save(submission);
			try {
				startThreads();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		return submission;
	}
}
