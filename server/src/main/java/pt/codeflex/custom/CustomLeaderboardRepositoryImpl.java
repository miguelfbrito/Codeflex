package pt.codeflex.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.codeflex.databasemodels.Leaderboard;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Users;
import pt.codeflex.models.TournamentLeaderboard;

public class CustomLeaderboardRepositoryImpl implements CustomLeaderboardRepository{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Leaderboard> findHighestScoreByUserByProblem(Users user, Problem problem) {
		
		Query query = em.createNativeQuery("select ifnull(l.id,0) as id, ifnull(max(l.score),-1) as score from users u, problem p, leaderboard l where\r\n" + 
				"l.user_id = :userId and\r\n" + 
				"l.problem_id = :problemId ;", "HighestScoreByUserByProblemMapping");
		query.setParameter("userId", user.getId());
		query.setParameter("problemId", problem.getId());
		
		return query.getResultList();
	}

	@Override
	public List<TournamentLeaderboard> getInformationForTournamentLeaderboard(long tournamentId) {
		Query query = em.createNativeQuery("select u.username as username, max(score) as score, d.opening_date as openingDate, "
				+ "d.completion_date as completionDate from submissions s, users u, durations d where s.problem_id in "
				+ "(select p.id from problem p where p.tournament_id = :tournamentId ) and s.users_id \r\n" + 
				"in (select users_id from rating r where r.tournament_id = :tournamentId ) and "
				+ "s.users_id = u.id and s.users_id = d.users_id and s.problem_id = d.problems_id "
				+ "group by s.users_id, s.problem_id;", "TournamentLeaderboardMapping");
		query.setParameter("tournamentId", tournamentId);
		
		return query.getResultList();
	}

}
