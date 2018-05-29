package pt.codeflex.models;

import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;

import pt.codeflex.databasemodels.Problem;



public class ListCategoriesWithStats {

	private long id;
	private String name;
	private int finishedProblems;
	private int totalProblems;
	
	public ListCategoriesWithStats(long id, String name, int finishedProblems, int totalProblems) {
		super();
		this.id = id;
		this.name = name;
		this.finishedProblems = finishedProblems;
		this.totalProblems = totalProblems;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFinishedProblems() {
		return finishedProblems;
	}
	public void setFinishedProblems(int finishedProblems) {
		this.finishedProblems = finishedProblems;
	}
	public int getTotalProblems() {
		return totalProblems;
	}
	public void setTotalProblems(int totalProblems) {
		this.totalProblems = totalProblems;
	}
	
	@PersistenceContext
	private EntityManager em;

	// TODO : critico, corrigir o SQL Injection
	public List<ListCategoriesWithStats> getAllByUserId(long userId) {
		Query query = em.createNativeQuery("select pc.id, pc.name, count(distinct p.name) as finishedProblems, (select count(*) from practise_category pc1, problem p1 where\r\n" + 
				"pc1.id = p1.practise_category_id  and pc1.id = pc.id group by pc1.name) as totalProblems from users u, submissions s, problem p, practise_category pc where \r\n" + 
				"u.id = s.users_id and  \r\n" + 
				"s.problem_id = p.id and  \r\n" + 
				"p.practise_category_id = pc.id and u.id = " + userId + " group by pc.name", ListCategoriesWithStats.class);
		return query.getResultList();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
