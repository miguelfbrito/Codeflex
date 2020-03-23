package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pt.codeflex.databasecompositekeys.RatingID;
import pt.codeflex.models.Leaderboard;
import pt.codeflex.models.Rating;
import pt.codeflex.repositories.LeaderboardRepository;
import pt.codeflex.repositories.RatingRepository;

@Service
public class LeaderboardService extends CRUDService<Leaderboard, Long> {


    private LeaderboardRepository repository;

    @Autowired
    public LeaderboardService(CrudRepository<Leaderboard, Long> repository) {
        super(repository);
    }


}
