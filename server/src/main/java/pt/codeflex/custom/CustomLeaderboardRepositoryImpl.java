package pt.codeflex.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.codeflex.databasemodels.Leaderboard;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Users;

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

}
