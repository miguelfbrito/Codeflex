package pt.codeflex.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
public class Problem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	@Size(min = 5, max = 50)
	@Pattern(regexp = "([A-Za-z0-9:_\\s]+)")
	private String name;

	@Column(length = 5000)
	private String description;

	@Column(length = 5000)
	private String inputFormat;
	
	@Column(length = 5000)
	private String outputFormat;

	@Column(length = 5000)
	private String constraints;
	
	private Date creationDate;

	private int maxScore;

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private Tournament tournament;

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private Users owner;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "problem_id")
	private List<TestCase> testCases = new ArrayList<>();

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
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

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
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

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	@Override
	public String toString() {
		return "Problem [id=" + id + ", name=" + name + ", description=" + description + ", inputFormat=" + inputFormat
				+ ", outputFormat=" + outputFormat + ", constraints=" + constraints + ", creationDate=" + creationDate
				+ ", maxScore=" + maxScore + ", tournament=" + tournament + ", owner=" + owner + ", testCases="
				+ testCases + ", difficulty=" + difficulty + "]";
	}

}
