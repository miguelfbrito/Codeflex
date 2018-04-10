package pt.codeflex.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Submissions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Date date;
	private String language;

	@Column(length = 10000)
	private String code;

//	@OneToMany
//	@JoinColumn(name = "submissions_id")
//	private List<Scoring> scoring = new ArrayList<>();

//	public List<Scoring> getScoring() {
//		return scoring;
//	}
//
//	public void setScoring(List<Scoring> scoring) {
//		this.scoring = scoring;
//	}

	public Submissions() {
	}

	
	public Submissions(Date date, String language, String code) {
		this.date = date;
		this.language = language;
		this.code = code;
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
