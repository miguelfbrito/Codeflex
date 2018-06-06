package pt.codeflex.custom;

import java.util.List;

import pt.codeflex.databasemodels.Leaderboard;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Users;

public interface CustomLeaderboardRepository {
	List<Leaderboard> findHighestScoreByUserByProblem(Users user, Problem problem);
}
