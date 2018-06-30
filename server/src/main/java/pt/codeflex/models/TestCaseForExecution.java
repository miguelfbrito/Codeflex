package pt.codeflex.models;

import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.TestCases;

public class TestCaseForExecution {

	private TestCases testCase;
	private Submissions submission;
	private String fileName;
	
	public TestCaseForExecution(TestCases testCase, Submissions submission, String fileName) {
		super();
		this.testCase = testCase;
		this.submission = submission;
		this.fileName = fileName;
	}
	
	public TestCases getTestCase() {
		return testCase;
	}
	public void setTestCase(TestCases testCase) {
		this.testCase = testCase;
	}
	public Submissions getSubmission() {
		return submission;
	}
	public void setSubmission(Submissions submission) {
		this.submission = submission;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
