package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ScoringID implements Serializable {
	
	private long submissions;
	private long testcases;

	public ScoringID() {}
	public ScoringID(long submission, long testCase) {
		this.submissions = submission;
		this.testcases = testCase;
	}
	
	public long getSubmission() {
		return submissions;
	}
	public void setSubmission(long submission) {
		this.submissions = submission;
	}
	public long getTestCase() {
		return testcases;
	}
	public void setTestCase(long testCase) {
		this.testcases = testCase;
	}
	
	
}
