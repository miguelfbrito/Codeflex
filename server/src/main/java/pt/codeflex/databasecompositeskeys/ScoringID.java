package pt.codeflex.databasecompositeskeys;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (submissions ^ (submissions >>> 32));
		result = prime * result + (int) (testcases ^ (testcases >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScoringID other = (ScoringID) obj;
		if (submissions != other.submissions)
			return false;
		if (testcases != other.testcases)
			return false;
		return true;
	}
	
	
	
	
}
