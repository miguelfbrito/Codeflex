package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.custom.CustomLeaderboardRepository;
import pt.codeflex.models.Leaderboard;
import pt.codeflex.models.Problem;

@CrossOrigin
@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long>, CustomLeaderboardRepository{
	List<Leaderboard> findAllByProblem(Problem problem);
}
