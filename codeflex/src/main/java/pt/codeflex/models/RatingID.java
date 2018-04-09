package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class RatingID implements Serializable {

	private long tournament;
	private long user;

	public RatingID(long tournament, long user) {
		super();
		this.tournament = tournament;
		this.user = user;
	}

	public long getTournament() {
		return tournament;
	}

	public void setTournament(long tournament) {
		this.tournament = tournament;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

}
