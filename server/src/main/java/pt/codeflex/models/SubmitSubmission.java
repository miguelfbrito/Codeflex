package pt.codeflex.models;

import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Users;

public class SubmitSubmission {

	private String code;
	private String language;
	private Users users;
	private Problem problem;

	public SubmitSubmission() {}
	public SubmitSubmission(String code, String language, Users users, Problem problem) {
		super();
		this.code = code;
		this.language = language;
		this.users = users;
		this.problem = problem;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}
}
