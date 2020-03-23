package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import pt.codeflex.databasecompositekeys.ProblemTournamentID;

@IdClass(ProblemTournamentID.class)
@Entity
public class ProblemTournament implements Serializable {

	@Id
	@ManyToOne
	private Problem problem;

	@Id
	@ManyToOne
	private Tournament tournament;

	public ProblemTournament(Problem problem, Tournament tournament) {
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
