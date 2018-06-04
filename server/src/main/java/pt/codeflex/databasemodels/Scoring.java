package pt.codeflex.databasemodels;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import pt.codeflex.databasecompositeskeys.ScoringID;

@IdClass(ScoringID.class)
@Entity
public class Scoring implements Serializable {

	@Id
	@ManyToOne
	private Submissions submissions;

	@Id
	@ManyToOne
	private TestCases testcases;

	private double value;
	private int isRight;
	private Date date;
	
	public Scoring() {
	}

	public Scoring(Submissions submission, TestCases testCase) {
		this.submissions = submission;
		this.testcases = testCase;
	}
	
	public Scoring(Submissions submission, TestCases testCase, double value, int isRight) {
		this.submissions = submission;
		this.testcases = testCase;
		this.value = value;
		this.isRight = isRight;
		this.date = Calendar.getInstance().getTime();
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}


	public Submissions getSubmissions() {
		return submissions;
	}

	public void setSubmissions(Submissions submissions) {
		this.submissions = submissions;
	}

	public TestCases getTestcases() {
		return testcases;
	}

	public void setTestcases(TestCases testcases) {
		this.testcases = testcases;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Scoring [submissions=" + submissions + ", testcases=" + testcases + ", value=" + value + ", isRight="
				+ isRight + ", date=" + date + "]";
	}

	public int getIsRight() {
		return isRight;
	}

	public void setIsRight(int isRight) {
		this.isRight = isRight;
	}
}
