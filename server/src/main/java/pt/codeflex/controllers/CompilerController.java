package pt.codeflex.controllers;

import pt.codeflex.databasemodels.Language;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.Users;
import pt.codeflex.evaluatesubmissions.*;
import pt.codeflex.models.Host;
import pt.codeflex.models.SubmitSubmission;
import pt.codeflex.repositories.LanguageRepository;
import pt.codeflex.repositories.ProblemRepository;
import pt.codeflex.repositories.SubmissionsRepository;
import pt.codeflex.repositories.UsersRepository;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.transaction.Transactional;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CompilerController {

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
	private LanguageRepository languageRepository;

	@Autowired
	private EvaluateSubmissions evaluateSubmissions;

	@Autowired
	private Host host;

	@GetMapping("/compiler")
	public String compiler() {
		return "compiler";
	}

	@GetMapping("/test")
	public void cpuUsage() throws IOException {
		System.out.println(host.getCpuUsage());
		System.out.println(host.getMemoryUsage());
	}

	@GetMapping("/ssh")
	public void startThreads() throws IOException, InterruptedException {
		long inicial = System.currentTimeMillis();

		evaluateSubmissions.setHost(host);
		List<Submissions> submissions = evaluateSubmissions.getSubmissions();

		System.out.println(submissions.toString());

		System.out.println("\n\n\nStarting thread  with host " + host.getIp() + "\n");
		EvaluateSubmissions evaluateSubmissions1 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions1.setHost(host);

		for (Submissions s : submissions) {

			evaluateSubmissions1.setSubmission(s);
			taskExecutor.execute(evaluateSubmissions1);

		}

		// for (Host h : hostList) {
		// // System.out.println("Starting thread with host " + h.getIp() + "\n");
		// EvaluateSubmissions evaluateSubmissions2 =
		// applicationContext.getBean(EvaluateSubmissions.class);
		// evaluateSubmissions2.setHost(h);
		// taskExecutor.execute(evaluateSubmissions1);
		// System.out.println("\n\n\n");
		// }

		// Adds all the submissions into a volatile queue. The queue is then distributed
		// to the threads

		// EvaluateSubmissions evaluateSubmissions3 =
		// applicationContext.getBean(EvaluateSubmissions.class);
		// evaluateSubmissions3.setHost(host1);
		// evaluateSubmissions3.connect(host1);
		// taskExecutor.execute(evaluateSubmissions3);
		//
		// EvaluateSubmissions evaluateSubmissions4 =
		// applicationContext.getBean(EvaluateSubmissions.class);
		// evaluateSubmissions4.setHost(host2);
		// evaluateSubmissions4.connect(host2);
		// taskExecutor.execute(evaluateSubmissions4);

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
