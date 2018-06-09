package pt.codeflex.models;

import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Tournament;

public class AddTournamentToProblem {

	private Problem problem;
	private Tournament tournament;

	public AddTournamentToProblem() {
	}

	public AddTournamentToProblem(Problem problem, Tournament tournament) {
		super();
		this.problem = problem;
		this.tournament = tournament;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

}
