package pt.codeflex.databasemodels;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;

import pt.codeflex.models.ListCategoriesWithStats;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;

@SqlResultSetMapping(name = "CategoriesWithStatsMapping", classes = @ConstructorResult(targetClass = ListCategoriesWithStats.class, columns = {
	    @ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "name"),
	    @ColumnResult(name = "finishedProblems", type = Integer.class), @ColumnResult(name = "totalProblems", type = Integer.class)}))

@Entity
public class PractiseCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String name;

	public PractiseCategory(String name) {
		this.name = name;
	}

	@OneToMany
	@JoinColumn(name = "practise_category_id")
	private List<Problem> problem = new ArrayList<>();

	public List<Problem> getProblem() {
		return problem;
	}

	public void setProblem(List<Problem> problem) {
		this.problem = problem;
	}

	public PractiseCategory() {
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

	@Override
	public String toString() {
		return "PractiseCategory [id=" + id + ", name=" + name + ", problem=" + problem + "]";
	}
	
	
}
