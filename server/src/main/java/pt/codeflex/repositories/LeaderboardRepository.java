package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Leaderboard;

@Repository
public interface LeaderboardRepository extends CrudRepository<Leaderboard, Long>{

}
