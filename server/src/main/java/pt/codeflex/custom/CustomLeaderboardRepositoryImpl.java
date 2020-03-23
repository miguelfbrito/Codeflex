package pt.codeflex.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.codeflex.models.Leaderboard;
import pt.codeflex.models.Problem;
import pt.codeflex.models.Users;
import pt.codeflex.dto.TournamentLeaderboard;

public class CustomLeaderboardRepositoryImpl implements CustomLeaderboardRepository{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Leaderboard> findHighestScoreByUserByProblem(Users users, Problem problem) {
		
		Query query = em.createNativeQuery("select ifnull(l.id,0) as id, ifnull(max(l.score),-1) as score from users u, problem p, leaderboard l where\r\n" + 
				"l.user_id = :userId and\r\n" + 
				"l.problem_id = :problemId ;", "HighestScoreMapping");
		query.setParameter("userId", users.getId());
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
