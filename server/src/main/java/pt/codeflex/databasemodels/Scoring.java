package pt.codeflex.databasemodels;

import java.io.Serializable;

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
}
