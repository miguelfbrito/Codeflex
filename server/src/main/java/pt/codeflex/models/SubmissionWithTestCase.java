package pt.codeflex.models;

import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.TestCases;

public class SubmissionWithTestCase {

	private Submissions submission;
	private TestCases testCase;

	public Submissions getSubmission() {
		return submission;
	}

	public void setSubmission(Submissions submission) {
		this.submission = submission;
	}


	public SubmissionWithTestCase(Submissions submission, TestCases testCase) {
		super();
		this.submission = submission;
		this.testCase = testCase;
	}

	public TestCases getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCases testCase) {
		this.testCase = testCase;
	}

}
