package pt.codeflex.databasemodels;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.transaction.Transactional;

@Entity
@Transactional
public class Submissions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Date date;
	private double score;
	@ManyToOne(fetch = FetchType.EAGER)
	private Language language;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Result result;

	@ManyToOne(fetch = FetchType.LAZY)
	private Problem problem;

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@Column(length = 10000)
	private String code;

	public Submissions() {
	}

	public Submissions(Problem problem, Language language, String code) {
		this.problem = problem;
		this.language = language;
		this.code = code;
	}
	
	public Submissions(Date date, double score, Language language, Result result, Problem problem, String code) {
		super();
		this.date = date;
		this.score = score;
		this.language = language;
		this.result = result;
		this.problem = problem;
		this.code = code;
	}

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}
