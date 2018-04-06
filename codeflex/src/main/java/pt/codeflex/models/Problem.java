package pt.codeflex.models;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Problem {

	@Id
	@SequenceGenerator(name = "seq_problem_id", sequenceName = "seq_problem_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_problem_id")
	private long id;

	private String name;
	private String description;

	@OneToMany
	private List<TestCases> testCases = new ArrayList<TestCases>();
	
	public Problem() {
	}

	public Problem(String name, String description) {
		this.name = name;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TestCases> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCases> testCases) {
		this.testCases = testCases;
	}

}
