package pt.codeflex.models;

import pt.codeflex.databasemodels.Difficulty;
import pt.codeflex.databasemodels.Problem;

public class ProblemDifficulty {
	private Problem problem;
	private Difficulty difficulty;

	public Problem getProblem() {
		return problem;
	}

	public ProblemDifficulty() {

	}

	public ProblemDifficulty(Problem problem, Difficulty difficulty) {
		super();
		this.problem = problem;
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
}
