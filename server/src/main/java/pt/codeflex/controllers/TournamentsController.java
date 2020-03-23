package pt.codeflex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.codeflex.dto.ProblemDetails;
import pt.codeflex.dto.TestCasesShown;
import pt.codeflex.models.Problem;
import pt.codeflex.models.Rating;
import pt.codeflex.models.Tournament;
import pt.codeflex.models.Users;
import pt.codeflex.services.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/tournaments")
public class TournamentsController extends RESTController<Tournament, Long> {

    // TODO: Refactor responses into ResponseBody or other structure more friendly to consume

    private TournamentService tournamentService;
    private RatingService ratingService;
    private UserService userService;

    @Autowired
    public TournamentsController(CRUDService<Tournament, Long> service, TournamentService tournamentService,
                                 RatingService ratingService, UserService userService) {
        super(service);
        this.tournamentService = tournamentService;
        this.ratingService = ratingService;
        this.userService = userService;
    }


    @GetMapping(path = "/{tournamentId}/users")
    public List<Users> viewUsersByTournamentId(@PathVariable long tournamentId) {
        return tournamentService.getAllUsersByTournament(tournamentId);
    }


}
