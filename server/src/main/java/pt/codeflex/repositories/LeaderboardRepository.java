package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.custom.CustomLeaderboardRepository;
import pt.codeflex.databasemodels.Leaderboard;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Users;
@CrossOrigin
@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long>, CustomLeaderboardRepository{
	List<Leaderboard> findAllByProblem(Problem problem);
}
