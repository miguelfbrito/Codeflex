package pt.codeflex.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class CustomTournamentRepositoryImpl implements CustomTournamentRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public double findScoreOfUserInTournament(long userId, long tournamentId) {

		Query query = em.createNativeQuery(
				"select sum(l.score) from leaderboard l where l.problem_id in (select p.id from problem p where p.tournament_id = :tournamentId ) and l.user_id = :userId ;");

		query.setParameter("tournamentId", tournamentId);
		query.setParameter("userId", userId);

		return (double) query.getResultList().get(0);
	}

}
