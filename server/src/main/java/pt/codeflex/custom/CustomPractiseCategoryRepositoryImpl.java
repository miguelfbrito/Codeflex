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
				" pc1.id = p1.practise_category_id  and pc1.id = pc.id group by pc1.name) as totalProblems from submissions s, problem p, practise_category pc where \r\n" + 
				"s.users_id = " + userId + " and  \r\n" + 
				" s.problem_id = p.id and  \r\n" + 
				" p.practise_category_id = pc.id and (select r.name from result r where r.id = s.result_id) = 'Correct' \r\n" + 
				" group by pc.name;", "CategoriesWithStatsMapping");
		
		return query.getResultList();
	}

}
