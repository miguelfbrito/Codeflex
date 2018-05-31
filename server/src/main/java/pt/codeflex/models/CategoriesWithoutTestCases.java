package pt.codeflex.models;

import java.util.List;

import pt.codeflex.databasemodels.Problem;

public class CategoriesWithoutTestCases {

	private long id;
	private String name;
	private List<ProblemWithoutTestCases> problem;
	
	public CategoriesWithoutTestCases(long id, String name, List<ProblemWithoutTestCases> problem) {
		super();
		this.id = id;
		this.name = name;
		this.problem = problem;
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

	public List<ProblemWithoutTestCases> getProblem() {
		return problem;
	}

	public void setProblem(List<ProblemWithoutTestCases> problem) {
		this.problem = problem;
	}

}
