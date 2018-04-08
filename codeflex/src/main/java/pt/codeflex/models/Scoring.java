package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@IdClass(ScoringID.class)
@Entity
public class Scoring implements Serializable {
	
	@Id
	@ManyToOne
	private Submissions submission;
	
	@Id
	@ManyToOne
	private TestCases testCase;
	
	public Scoring() {}
	
	public Scoring(Submissions submission, TestCases testCase) {
		this.submission = submission;
		this.testCase = testCase;
	}

	public Submissions getSubmission() {
		return submission;
	}

	public void setSubmission(Submissions submission) {
		this.submission = submission;
	}

	public TestCases getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCases testCase) {
		this.testCase = testCase;
	}
}
