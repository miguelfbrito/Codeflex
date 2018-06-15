package pt.codeflex.models;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import pt.codeflex.databasemodels.Submissions;

@Component
public class TasksBeingEvaluated {

	private List<SubmissionWithTestCase> tasksBeingEvaluated;

	public TasksBeingEvaluated() {
	}

	public TasksBeingEvaluated(List<SubmissionWithTestCase> submissions) {
		super();
		this.tasksBeingEvaluated = submissions;
	}

	@Bean
	public List<SubmissionWithTestCase> getSubmissionsWithTestCases() {
		return tasksBeingEvaluated;
	}

	public void setSubmissionsWithTestCases(List<SubmissionWithTestCase> submissionsBeingEvaluated) {
		this.tasksBeingEvaluated = submissionsBeingEvaluated;
	}

}
