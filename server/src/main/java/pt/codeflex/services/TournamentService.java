package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pt.codeflex.databasecompositekeys.RatingID;
import pt.codeflex.models.Difficulty;
import pt.codeflex.models.Rating;
import pt.codeflex.models.Tournament;
import pt.codeflex.models.Users;
import pt.codeflex.repositories.DifficultyRepository;
import pt.codeflex.repositories.TournamentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService extends CRUDService<Tournament, Long> {


    private TournamentRepository repository;
    private RatingService ratingService;

    @Autowired
    public TournamentService(CrudRepository<Tournament, Long> repository) {
        super(repository);
    }

    public Tournament getByName(String name) {
        return repository.findByName(name);
    }

    public boolean isUserRegistered(long tournamentId, long userId) {
        Tournament tournament = get(tournamentId);
        Rating rating = ratingService.get(new RatingID(tournamentId, userId));
        if (tournament == null)
            return false;

        return rating != null;
    }


    public List<Users> getAllUsersByTournament(long tournamentId) {

        List<Users> usersInTournament = new ArrayList<>();
        List<Rating> allRatings = ratingService.getAll();
        Tournament tournament = get(tournamentId);

        if (tournament == null) {
            return usersInTournament;
        }

        for (Rating r : allRatings) {
            if (r.getTournament().getId() == tournament.getId()) {
                usersInTournament.add(r.getUsers());
            }
        }

        return usersInTournament;
    }


}
