package pt.codeflex.databasecomposites;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class RatingID implements Serializable {

	private long tournament;
	private long users;

	public RatingID(long tournament, long user) {
		super();
		this.tournament = tournament;
		this.users = user;
	}

	public long getTournament() {
		return tournament;
	}

	public void setTournament(long tournament) {
		this.tournament = tournament;
	}

	public long getUser() {
		return users;
	}

	public void setUser(long user) {
		this.users = user;
	}

}
