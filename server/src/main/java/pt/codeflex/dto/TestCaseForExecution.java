package pt.codeflex.dto;

import pt.codeflex.models.Submissions;
import pt.codeflex.models.TestCase;

public class TestCaseForExecution {

	private TestCase testCase;
	private Submissions submission;
	private String fileName;
	
	public TestCaseForExecution(TestCase testCase, Submissions submission, String fileName) {
		super();
		this.testCase = testCase;
		this.submission = submission;
		this.fileName = fileName;
	}
	
	public TestCase getTestCase() {
		return testCase;
	}
	public void setTestCase(TestCase testCase) {
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
