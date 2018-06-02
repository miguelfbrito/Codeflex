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

@Entity
public class Submissions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Date date;
	private String language;
	private boolean isRight;
	
	@ManyToOne
	private Result result;

	@ManyToOne(fetch = FetchType.EAGER)
	private Problem problem;

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@Column(length = 10000)
	private String code;

	// @OneToMany
	// @JoinColumn(name = "submissions_id")
	// private List<Scoring> scoring = new ArrayList<>();

	// public List<Scoring> getScoring() {
	// return scoring;
	// }
	//
	// public void setScoring(List<Scoring> scoring) {
	// this.scoring = scoring;
	// }

	public Submissions() {
	}

	public Submissions(Problem problem, String language, String code) {
		this.problem = problem;
		this.date = Calendar.getInstance().getTime();
		this.language = language;
		this.code = code;
		this.isRight = false;
	}

	public Submissions(Problem problem, String language, String code, boolean isRight) {
		this.problem = problem;
		this.date = Calendar.getInstance().getTime();
		this.language = language;
		this.code = code;
		this.isRight = isRight;
	}

	public Submissions(long id, String code, String language) {
		this.id = id;
		this.language = language;
		this.code = code;
	}
	
	public Submissions(String language, String code) {
		this.date = Calendar.getInstance().getTime();
		this.language = language;
		this.code = code;
		this.isRight = false;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
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

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
