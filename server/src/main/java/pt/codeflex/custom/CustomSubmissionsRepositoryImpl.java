package pt.codeflex.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.codeflex.models.Submissions;

public class CustomSubmissionsRepositoryImpl implements CustomSubmissionsRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Submissions> findSubmissionsToAvaliate() {
		Query query = em.createNativeQuery(
				"select s.id from submissions s where s.id " + "not in (select sc.submissions_id from scoring sc)",
				"SubmissionsToEvaluateMapping");
		return query.getResultList();
	}

}
