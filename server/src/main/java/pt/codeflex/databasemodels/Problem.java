package pt.codeflex.databasemodels;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Problem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String name;
	private String description;

	private String inputFormat;
	private String outputFormat;
	private String constraints;
	private Date creationDate;

	private int maxScore;

	@ManyToOne
	private Users owner;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "problem_id")
	private List<TestCases> testCases = new ArrayList<>();

	@ManyToOne
	private Difficulty difficulty;

	public Problem() {
		this.creationDate = Calendar.getInstance().getTime();
	}

	public Problem(String name, String description) {
		this.name = name;
		this.description = description;
		this.maxScore = 10;
		this.creationDate = Calendar.getInstance().getTime();
	}

	public Problem(String name, String description, Difficulty difficulty) {
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.maxScore = 10;
		this.creationDate = Calendar.getInstance().getTime();
	}

	public Problem(String name, String description, Difficulty difficulty, int maxScore) {
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.maxScore = maxScore;
		this.creationDate = Calendar.getInstance().getTime();
	}

	public Problem(String name, String description, String inputFormat, String outputFormat, String constraints,
			int maxScore, Users owner, Difficulty difficulty) {
		this.name = name;
		this.description = description;
		this.inputFormat = inputFormat;
		this.outputFormat = outputFormat;
		this.constraints = constraints;
		this.maxScore = maxScore;
		this.owner = owner;
		this.difficulty = difficulty;
		this.creationDate = Calendar.getInstance().getTime();
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

	public String getInputFormat() {
		return inputFormat;
	}

	public void setInputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}

	public Users getOwner() {
		return owner;
	}

	public void setOwner(Users owner) {
		this.owner = owner;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
