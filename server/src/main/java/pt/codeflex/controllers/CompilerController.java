package pt.codeflex.controllers;

import pt.codeflex.models.Language;
import pt.codeflex.models.Problem;
import pt.codeflex.models.Submissions;
import pt.codeflex.models.Users;
import pt.codeflex.evaluatesubmissions.*;
import pt.codeflex.dto.Host;
import pt.codeflex.dto.SubmitSubmission;
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

    private TaskExecutor taskExecutor;
    private ApplicationContext applicationContext;
    private SubmissionsRepository submissionsRepository;
    private ProblemRepository problemRepository;
    private UsersRepository usersRepository;
    private LanguageRepository languageRepository;
    private Host host;

    // TODO : refactor into usage of services instead of repositories
    @Autowired
    public CompilerController(TaskExecutor taskExecutor, ApplicationContext applicationContext, SubmissionsRepository submissionsRepository,
                              ProblemRepository problemRepository, UsersRepository usersRepository, LanguageRepository languageRepository, Host host) {

        this.taskExecutor = taskExecutor;
        this.applicationContext = applicationContext;
        this.submissionsRepository = submissionsRepository;
        this.problemRepository = problemRepository;
        this.usersRepository = usersRepository;
        this.languageRepository = languageRepository;
        this.host = host;
    }

    @Transactional
    @PostMapping("/submission")
    public Submissions submit(@RequestBody SubmitSubmission submitSubmission) {

        Problem problem = problemRepository.findByName(submitSubmission.getProblem().getName().replaceAll("-", " "));
        Users users = usersRepository.findByUsername(submitSubmission.getUsers().getUsername());
        Language language = languageRepository.findByName(submitSubmission.getLanguage().getName());

        Submissions submission = new Submissions();
        if (problem != null && language != null && users != null) {

            submission = new Submissions(problem, language, submitSubmission.getCode(), users);
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
