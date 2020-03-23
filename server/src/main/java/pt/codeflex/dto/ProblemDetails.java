package pt.codeflex.dto;

import pt.codeflex.models.*;
import pt.codeflex.models.Users;

public class ProblemDetails {
	private Problem problem;
	private Difficulty difficulty;
	private Users owner;
	private Tournament tournament;
	private PractiseCategory category;

	public ProblemDetails() {
	}

	public ProblemDetails(Problem problem, Difficulty difficulty) {
		super();
		this.problem = problem;
		this.difficulty = difficulty;
	}

	public ProblemDetails(Problem problem, Difficulty difficulty, Users owner) {
		super();
		this.problem = problem;
		this.owner = owner;
		this.difficulty = difficulty;
	}

	public ProblemDetails(Problem problem, Difficulty difficulty, Users owner, Tournament tournament) {
		super();
		this.problem = problem;
		this.owner = owner;
		this.difficulty = difficulty;
		this.tournament = tournament;
	}
	
	public ProblemDetails(Problem problem, Difficulty difficulty, Users owner, Tournament tournament, PractiseCategory category) {
		super();
		this.problem = problem;
		this.owner = owner;
		this.difficulty = difficulty;
		this.tournament = tournament;
		this.category = category;
	}

	public Problem getProblem() {
		return problem;
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

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public PractiseCategory getCategory() {
		return category;
	}

	public void setCategory(PractiseCategory category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "AddProblem [problem=" + problem + ", difficulty=" + difficulty + ", owner=" + owner + ", tournament="
				+ tournament + ", category=" + category + "]";
	}

	
}
