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

import java.util.ArrayDeque;
import java.util.Queue;

import javax.transaction.Transactional;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	private Host host;

	@Transactional
	@PostMapping("/submission")
	public Submissions submit(@RequestBody SubmitSubmission submitSubmission) {

		Problem problem = problemRepository.findByName(submitSubmission.getProblem().getName().replaceAll("-", " "));
		Users user = usersRepository.findByUsername(submitSubmission.getUsers().getUsername());
		Language language = languageRepository.findByName(submitSubmission.getLanguage().getName());

		Submissions submission = new Submissions();
		if (problem != null && language != null && user != null) {

			submission = new Submissions(problem, language, submitSubmission.getCode(), user);
			submissionsRepository.save(submission);

			startThread();
		}

		return submission;
	}

	public void startThread() {
		EvaluateSubmissions evaluateSubmissions1 = applicationContext.getBean(EvaluateSubmissions.class);
		evaluateSubmissions1.setHost(host);
		evaluateSubmissions1.getSubmissions();
		taskExecutor.execute(evaluateSubmissions1);
	}

}
