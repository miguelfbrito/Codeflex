package pt.codeflex.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Submissions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private Date date;
	private String language;
	
	@Column(length = 10000)
	private String code;

	@ManyToOne
	private Users user;
	
	@ManyToOne
	private Problem problem;

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Submissions() {
	}

	public Submissions(Users user, Date date, String language, String code) {
		this.date = date;
		this.language = language;
		this.code = code;
		this.setUser(user);
	}
	

	public Submissions(Problem problem, Users user, Date date, String language, String code) {
		this.date = date;
		this.language = language;
		this.code = code;
		this.setUser(user);
		this.setProblem(problem);
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
}
