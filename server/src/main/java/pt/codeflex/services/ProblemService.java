package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pt.codeflex.dto.AddTournamentToProblem;
import pt.codeflex.dto.ProblemDetails;
import pt.codeflex.dto.TestCasesShown;
import pt.codeflex.models.*;
import pt.codeflex.repositories.ProblemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemService extends CRUDService<Problem, Long> {


    private ProblemRepository problemRepository;

    private UserService userService;
    private PractiseCategoryService practiseCategoryService;
    private DifficultyService difficultyService;
    private TournamentService tournamentService;
    private TestCaseService testCaseService;

    @Autowired
    public ProblemService(CrudRepository<Problem, Long> repository, PractiseCategoryService practiseCategoryService, DifficultyService difficultyService,
                          TournamentService tournamentService, UserService userService, TestCaseService testCaseService) {
        super(repository);
        this.practiseCategoryService = practiseCategoryService;
        this.difficultyService = difficultyService;
        this.tournamentService = tournamentService;
        this.userService = userService;
        this.testCaseService = testCaseService;
    }

    public Problem getByName(String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Problem p = problemRepository.findByName(name.replace("-", " "));
        Users user = userService.getByName(auth.getName());

        if (user != null && p != null) {
            if (p.getTournament() != null
                    && !tournamentService.isUserRegistered(p.getTournament().getId(), user.getId())) {
                return new Problem();
            }

            return p;
        }

        return null;
    }

    public List<Problem> getAllByOwner(Users owner) {
        return problemRepository.findAllByOwner(owner);
    }

    public ProblemDetails getProblemFullDetails(String name) {
        Problem problem = getByName(name);
        ProblemDetails problemDetails = new ProblemDetails();

        if (problem != null) {
            problemDetails.setProblem(problem);
            problemDetails.setCategory(practiseCategoryService.getPractiseCategoryByProblemName(problem.getName()));
        }

        return problemDetails;
    }


    public Problem add(ProblemDetails problemDetails) {

        // TODO: cleanup method
        Problem p = problemDetails.getProblem();

        if (p == null) {
            return new Problem();
        }

        if (problemDetails.getDifficulty() != null) {
            Difficulty f = difficultyService.getById(problemDetails.getDifficulty().getId());
            if (f != null) {
                p.setDifficulty(f);
            } else {
                throw new IllegalArgumentException("Difficulty not found!");
            }
        }

        // A problem can only belong to either a Tournament or a Category
        if (problemDetails.getTournament() != null && problemDetails.getCategory() != null) {
            if (problemDetails.getTournament().getName() != null && problemDetails.getCategory().getName() != null
                    && !problemDetails.getCategory().getName().equals("")) {
                return new Problem();
            }
        }

        if (problemDetails.getTournament() != null) {
            if (problemDetails.getTournament().getName() != null) {
                Tournament t = tournamentService.getByName(problemDetails.getTournament().getName());
                if (t != null) {
                    p.setTournament(t);
                } else {
                    throw new IllegalArgumentException("Tournament not found!");
                }
            }
        }

        if (problemDetails.getOwner() != null) {
            Users user = userService.getById(problemDetails.getOwner().getId());

            // TODO: review
            if (user != null) {
                p.setOwner(user);
            } else {
                Users users = userService.getByName(problemDetails.getOwner().getUsername());
                if (user != null) {
                    p.setOwner(users);
                }
            }

        } else {
            // TODO : remove. Adding a static user for now, debug purposes
            Users u = userService.getById((long) 2);
            if (u != null) {
                p.setOwner(u);
            }
        }

        Problem savedProblem = problemRepository.save(p);

        if (problemDetails.getCategory() != null) {
            PractiseCategory category = practiseCategoryService.getById(problemDetails.getCategory().getId());
            if (category != null) {
                category.getProblem().add(p);
                practiseCategoryService.create(category);
            }
        }

        return savedProblem;
    }


    public Problem update(ProblemDetails problemDetails) {

        Optional<Problem> prob = problemRepository.findById(problemDetails.getProblem().getId());
        if (!prob.isPresent()) {
            return null;
        }

        Problem updatedProblem = prob.get();
        Problem p = problemDetails.getProblem();

        // TODO : Move to Problem
        updatedProblem.setName(p.getName());
        updatedProblem.setDescription(p.getDescription());
        updatedProblem.setConstraints(p.getConstraints());
        updatedProblem.setInputFormat(p.getInputFormat());
        updatedProblem.setOutputFormat(p.getOutputFormat());
        updatedProblem.setMaxScore(p.getMaxScore());

        // updates difficulty
        Difficulty d = difficultyService.getById(problemDetails.getProblem().getDifficulty().getId());
        if (d != null) {
            updatedProblem.setDifficulty(d);
        }

        // updates category
        // 1. removes the problem from current category
        List<PractiseCategory> categories = practiseCategoryService.getAll();
        for (PractiseCategory pc : categories) {
            if (pc.getProblem().contains(p)) {
                if (pc.getName().equals(problemDetails.getCategory().getName())) {
                    break;
                }
                pc.getProblem().remove(p);
            }
        }

        practiseCategoryService.createAll(categories);

        // 2. adds the problem to the category sent
        PractiseCategory category = practiseCategoryService.getById(problemDetails.getCategory().getId());
        if (category != null) {
            category.getProblem().add(p);
            practiseCategoryService.create(category);
        }

        return problemRepository.save(updatedProblem);
    }


    public Problem addTestCase(long problemId, long testCaseId) {
        TestCase tc = testCaseService.get(testCaseId);
        Optional<Problem> p = problemRepository.findById(problemId);

        if (!p.isPresent() || tc == null) {
            return null;
        }

        Problem problem = p.get();
        problem.getTestCases().add(tc);
        problemRepository.save(problem);
        return problem;
    }


    public Problem addTournament(long problemId, long tournamentId) {
        Tournament tournament = tournamentService.get(tournamentId);
        // tournament.setShowWebsite(true);
        Problem problem = get(problemId);

        if (tournament == null || problem == null)
            return null;

        problem.setTournament(tournament);
        return problemRepository.save(problem);
    }


    public List<TestCasesShown> getAllTestCases(String problemName) {
        List<TestCasesShown> finalTestCases = new ArrayList<>();
        Problem p = getByName(problemName);

        if (p != null) {
            List<TestCase> testCases = p.getTestCases();
            for (TestCase tc : testCases) {
                finalTestCases.add(new TestCasesShown(tc.getId(), tc.getInput(), tc.getOutput(), tc.getDescription(),
                        tc.isShown()));
            }
        }
        return finalTestCases;
    }




}
