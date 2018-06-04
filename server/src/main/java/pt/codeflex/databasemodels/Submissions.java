package pt.codeflex.databasemodels;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;


@SqlResultSetMapping(name = "SubmissionsToAvaliateMapping", classes = @ConstructorResult(targetClass = Submissions.class, columns = {
		@ColumnResult(name = "id", type = Long.class) }))

@Entity
public class Submissions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Date date;
	private double score;

	@ManyToOne
	private Result result;

	@ManyToOne
	private Problem problem;

	@ManyToOne
	private Language language;

	@Column(length = 10000)
	private String code;

	public Submissions() {
	}

	public Submissions(long id) {
		this.id = id;
	}

	public Submissions(Problem problem, Language language, String code ) {
		this.problem = problem;
		this.language = language;
		this.code = code;
		this.date = Calendar.getInstance().getTime();
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

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
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

	@Override
	public String toString() {
		return "Submissions [id=" + id + ", date=" + date + ", score=" + score + ", result=" + result + ", problem="
				+ problem + ", language=" + language + ", code=" + code + "]";
	}
}
