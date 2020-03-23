package pt.codeflex.dto;

import pt.codeflex.models.Submissions;
import pt.codeflex.models.TestCase;

public class SubmissionWithTestCase {

	private Submissions submission;
	private TestCase testCase;

	public Submissions getSubmission() {
		return submission;
	}

	public void setSubmission(Submissions submission) {
		this.submission = submission;
	}

	public SubmissionWithTestCase(Submissions submission, TestCase testCase) {
		super();
		this.submission = submission;
		this.testCase = testCase;
	}

	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

}
