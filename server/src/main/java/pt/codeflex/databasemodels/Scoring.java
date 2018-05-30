package pt.codeflex.databasemodels;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import pt.codeflex.databasecomposites.ScoringID;

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
	private boolean isRight;
	private Date date;
	
	public Scoring() {
	}

	public Scoring(Submissions submission, TestCases testCase) {
		this.submissions = submission;
		this.testcases = testCase;
	}
	
	public Scoring(Submissions submission, TestCases testCase, double value, boolean isRight) {
		this.submissions = submission;
		this.testcases = testCase;
		this.value = value;
		this.isRight = isRight;
		this.date = Calendar.getInstance().getTime();
	}

	public Submissions getSubmission() {
		return submissions;
	}

	public void setSubmission(Submissions submission) {
		this.submissions = submission;
	}

	public TestCases getTestCase() {
		return testcases;
	}

	public void setTestCase(TestCases testCase) {
		this.testcases = testCase;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
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
}
