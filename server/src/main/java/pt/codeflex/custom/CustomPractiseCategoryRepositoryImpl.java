package pt.codeflex.custom;

import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;

import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.models.ListCategoriesWithStats;



public class CustomPractiseCategoryRepositoryImpl implements CustomPractiseCategoryRepository {

	@PersistenceContext
	private EntityManager em;

	// TODO : critical - fix sql injection
	
	
	
	@Override
	public List<ListCategoriesWithStats> listCategoriesWithStatsByUserId(long userId) {
		Query query = em.createNativeQuery("select pc.id, pc.name, count(distinct p.name) as finishedProblems, (select count(*) from practise_category pc1, problem p1 where\r\n" + 
				"pc1.id = p1.practise_category_id  and pc1.id = pc.id group by pc1.name) as totalProblems from users u, submissions s, problem p, practise_category pc where \r\n" + 
				"s.problem_id = p.id and  \r\n" + 
				"s.is_right = 1 and " +
				"p.practise_category_id = pc.id and s.users_id = " + userId + " group by pc.name", "CategoriesWithStatsMapping");
		
		return query.getResultList();
	}

}
