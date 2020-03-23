package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import pt.codeflex.databasecompositekeys.DurationsID;
import pt.codeflex.databasecompositekeys.RatingID;
import pt.codeflex.models.Durations;
import pt.codeflex.models.Problem;
import pt.codeflex.models.Users;
import pt.codeflex.repositories.DurationsRepository;

import java.time.Duration;
import java.util.Calendar;
import java.util.Optional;

@Service
public class DurationService extends CRUDService<Durations, DurationsID> {


    private DurationsRepository repository;

    private UserService userService;
    private ProblemService problemService;

    @Autowired
    public DurationService(CrudRepository<Durations, DurationsID> repository, UserService userService, ProblemService problemService) {
        super(repository);
        this.userService = userService;
        this.problemService = problemService;
    }


    public Durations addOnProblemOpening(Durations duration) {

        Users users = userService.getByName(duration.getUsers().getUsername());
        Problem problem = problemService.get(duration.getProblems().getId());
        Durations newDuration = new Durations();

        if (users != null && problem != null) {

            newDuration.setUsers(users);
            newDuration.setProblems(problem);

            boolean openedAlready = repository.existsById(new DurationsID(users.getId(), problem.getId()));

            if (openedAlready) {
                return new Durations();
            }

            newDuration.setOpeningDate(Calendar.getInstance().getTime());

            return create(newDuration);
        }

        return newDuration;
    }


    public Durations updateDurationsOnProblemCompletion(Durations duration) {

        Users users = userService.getByName(duration.getUsers().getUsername());
        Problem problem = problemService.get(duration.getProblems().getId());

        if (users != null && problem != null) {

            Optional<Durations> d = repository.findById(new DurationsID(users.getId(), problem.getId()));

            if (d.isPresent()) {
                Durations currentDuration = d.get();

                System.out.println(currentDuration.getCompletionDate());
                if (currentDuration.getCompletionDate() == null) {
                    currentDuration.setCompletionDate(Calendar.getInstance().getTime());
                    return create(currentDuration);
                }
            }
        }

        return new Durations();

    }


}
