package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ScoringID implements Serializable {
	
	private long submission;
	private long testCase;
	
	public ScoringID(long submission, long testCase) {
		this.submission = submission;
		this.testCase = testCase;
	}
	
	public long getSubmission() {
		return submission;
	}
	public void setSubmission(long submission) {
		this.submission = submission;
	}
	public long getTestCase() {
		return testCase;
	}
	public void setTestCase(long testCase) {
		this.testCase = testCase;
	}
	
	
}
