package pt.codeflex.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.codeflex.models.Submissions;

public class CustomSubmissionsRepositoryImpl implements CustomSubmissionsRepository {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Submissions> findSubmissionsToAvaliate() {
		Query query = em.createNativeQuery(
				"select s.id, s.code, s.date, s.language, s.problem_id, s.users_id from submissions s where s.id not in (select sc.submissions_id from scoring sc);", Submissions.class);
		return query.getResultList();
	}

}
