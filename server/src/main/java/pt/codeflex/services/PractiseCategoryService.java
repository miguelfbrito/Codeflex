package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pt.codeflex.models.PractiseCategory;
import pt.codeflex.models.Problem;
import pt.codeflex.repositories.PractiseCategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PractiseCategoryService extends CRUDService<PractiseCategory, Long> {


    private PractiseCategoryRepository practiseCategoryRepository;
    private ProblemService problemService;

    @Autowired
    public PractiseCategoryService(CrudRepository<PractiseCategory, Long> repository, @Lazy ProblemService problemService) {
        super(repository);
        this.problemService = problemService;
    }

    public PractiseCategory getByName(String name) {
        return practiseCategoryRepository.findByName(name);
    }

    public PractiseCategory getById(long id) {
        Optional<PractiseCategory> byId = practiseCategoryRepository.findById(id);
        return byId.orElse(null);
    }

    public List<Problem> getAllProblems() {
        // TODO: Consider changing to join (evaluate impact on server vs impact on db)
        List<Problem> finalProblems = new ArrayList<>();
        List<Problem> problems = problemService.getAll();
        List<PractiseCategory> categories = getAll();

        for (PractiseCategory pc : categories) {
            for (Problem p : problems) {
                if (pc.getProblem().contains(p)) {
                    finalProblems.add(p);
                }
            }
        }

        return finalProblems;
    }


    public PractiseCategory getPractiseCategoryByProblemName(String problemName) {
        List<PractiseCategory> allCategories = getAll();
        Problem problem = problemService.getByName(problemName);

        // TODO : improve search
        if (problem != null) {
            for (PractiseCategory pc : allCategories) {
                if (pc.getProblem().contains(problem)) {
                    return pc;
                }
            }

        }

        return null;
    }


}
