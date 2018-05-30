package pt.codeflex.databasemodels;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Problem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private String description;
	private int maxScore;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "problem_id")
	private List<TestCases> testCases = new ArrayList<>();

	@ManyToOne
	private Difficulty difficulty;

	public Problem() {
	}

	public Problem(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Problem(String name, String description, Difficulty difficulty) {
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
	}

	public Problem(String name, String description, Difficulty difficulty, int maxScore) {
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.maxScore = maxScore;
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

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public String toString() {
		return "Problem [id=" + id + ", name=" + name + ", description=" + description + ", testCases=" + testCases
				+ ", difficulty=" + difficulty + "]";
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

}
