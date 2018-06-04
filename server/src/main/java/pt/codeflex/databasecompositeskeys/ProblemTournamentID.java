package pt.codeflex.databasecompositeskeys;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ProblemTournamentID implements Serializable{
	
	private long problem;
	private long tournament;
	
	public ProblemTournamentID(long problem, long tournament) {
		super();
		this.problem = problem;
		this.tournament = tournament;
	}
	public long getProblem() {
		return problem;
	}
	public void setProblem(long problem) {
		this.problem = problem;
	}
	public long getTournament() {
		return tournament;
	}
	public void setTournament(long tournament) {
		this.tournament = tournament;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (problem ^ (problem >>> 32));
		result = prime * result + (int) (tournament ^ (tournament >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProblemTournamentID other = (ProblemTournamentID) obj;
		if (problem != other.problem)
			return false;
		if (tournament != other.tournament)
			return false;
		return true;
	}
}
