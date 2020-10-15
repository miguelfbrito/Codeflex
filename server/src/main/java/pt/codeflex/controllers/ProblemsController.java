package pt.codeflex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.codeflex.databasecompositekeys.DurationsID;
import pt.codeflex.dto.ProblemDetails;
import pt.codeflex.dto.TestCasesShown;
import pt.codeflex.models.Durations;
import pt.codeflex.models.Problem;
import pt.codeflex.models.Users;
import pt.codeflex.services.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/problems")
public class ProblemsController extends RESTController<Problem, Long> {

    // TODO: Refactor responses into ResponseBody or other structure more friendly to consume

    private ProblemService problemService;
    private UserService userService;
    private PractiseCategoryService practiseCategoryService;
    private DurationService durationService;

    @Autowired
    public ProblemsController(CRUDService<Problem, Long> service, ProblemService problemService, UserService userService,
                              PractiseCategoryService practiseCategoryService, DurationService durationService) {
        super(service);
        this.problemService = problemService;
        this.userService = userService;
        this.practiseCategoryService = practiseCategoryService;
        this.durationService = durationService;
    }

    @GetMapping(params = {"name"})
    public Problem getByName(@RequestParam String name) {
        return problemService.getByName(name);
    }

    @GetMapping(params = {"owner"})
    public List<Problem> getAllByUsername(@PathVariable String owner) {
        Users userOwner = userService.getByName(owner);

        if (userOwner != null)
            return problemService.getAllByOwner(userOwner);

        return new ArrayList<>();
    }

    @GetMapping("/practise")
    public List<Problem> getDetailsProblem() {
        return practiseCategoryService.getAllProblems();
    }

    @GetMapping("/{problemName}/full")
    public ProblemDetails getDetailsProblem(@PathVariable String problemName) {
        return problemService.getProblemFullDetails(problemName);
    }

    @PostMapping
    public Problem create(@RequestBody ProblemDetails problemDetails) {
        return problemService.add(problemDetails);
    }

    @PatchMapping
    public Problem update(@RequestBody ProblemDetails problemDetails) {
        return problemService.update(problemDetails);
    }

    @PostMapping("/{problemId}/testcase/{testcaseId}")
    public Problem addTestCase(@PathVariable long problemId, @PathVariable long testcaseId) {
        return problemService.addTestCase(problemId, testcaseId);
    }

    @PostMapping("/{problemId}/tournament/{tournamentId}")
    public Problem addTournament(@PathVariable long problemId, @PathVariable long tournamentId) {
        return problemService.addTournament(problemId, tournamentId);
    }

    @GetMapping("/{problemName}/testcases")
    public List<TestCasesShown> viewAllTestCasesByProblemName(@PathVariable String problemName) {
        return problemService.getAllTestCases(problemName);
    }


    // TODO : Refactor userId to authorization/token context
    @GetMapping("/{problemId}/duration/{userId}")
    public Durations getDuration(@PathVariable long problemId, @PathVariable long userId) {
        return durationService.get(new DurationsID(userId, problemId));
    }


    @PostMapping("/{problemId}/opening")
    public Durations addDurationOnProblemOpening(@RequestBody Durations duration) {
        return durationService.addOnProblemOpening(duration);
    }

    @PostMapping("/{problemId}/closing")
    public Durations updateDurationOnProblemCompletion(@RequestBody Durations duration) {
        return durationService.updateDurationsOnProblemCompletion(duration);
    }


}
