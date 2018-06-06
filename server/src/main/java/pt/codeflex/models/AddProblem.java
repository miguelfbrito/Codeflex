package pt.codeflex.models;

import pt.codeflex.databasemodels.Difficulty;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Users;

public class AddProblem {
	private Problem problem;
	private Difficulty difficulty;
	private Users owner;
	
	public Problem getProblem() {
		return problem;
	}

	public AddProblem() {

	}

	public AddProblem(Problem problem, Difficulty difficulty) {
		super();
		this.problem = problem;
		this.difficulty = difficulty;
	}

	public AddProblem(Problem problem, Difficulty difficulty, Users owner) {
		super();
		this.problem = problem;
		this.owner = owner;
		this.difficulty = difficulty;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Users getOwner() {
		return owner;
	}

	public void setOwner(Users owner) {
		this.owner = owner;
	}

}
