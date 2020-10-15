package pt.codeflex.custom;

import java.util.List;

import pt.codeflex.models.Leaderboard;
import pt.codeflex.models.Problem;
import pt.codeflex.models.Users;
import pt.codeflex.dto.TournamentLeaderboard;

public interface CustomLeaderboardRepository {
	List<Leaderboard> findHighestScoreByUserByProblem(Users users, Problem problem);
	List<TournamentLeaderboard> getInformationForTournamentLeaderboard(long tournamentId);
	
}
