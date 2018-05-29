package pt.codeflex.models;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


public class ListCategoriesWithStatsImpl implements ListCategoriesWithStatsInterface{
	
	@PersistenceContext
	private EntityManager em;

	// TODO : critico, corrigir o SQL Injection
	@Override
	public List<ListCategoriesWithStats> getAllByUserId(long userId) {
		Query query = em.createNativeQuery("select pc.id, pc.name, count(distinct p.name) as finishedProblems, (select count(*) from practise_category pc1, problem p1 where\r\n" + 
				"pc1.id = p1.practise_category_id  and pc1.id = pc.id group by pc1.name) as totalProblems from users u, submissions s, problem p, practise_category pc where \r\n" + 
				"u.id = s.users_id and  \r\n" + 
				"s.problem_id = p.id and  \r\n" + 
				"p.practise_category_id = pc.id and u.id = " + userId + " group by pc.name", ListCategoriesWithStats.class);
		return query.getResultList();
	}

}
