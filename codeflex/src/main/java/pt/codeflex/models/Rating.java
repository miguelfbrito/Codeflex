package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@IdClass(RatingID.class)
@Entity
public class Rating implements Serializable {

	@Id
	@ManyToOne
	private Tournament tournament;

	@Id
	@ManyToOne
	private Users user;

	private double elo;
	
	public Rating(Tournament tournament, Users user, double elo) {
		super();
		this.tournament = tournament;
		this.user = user;
		this.elo = elo;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public double getElo() {
		return elo;
	}

	public void setElo(double elo) {
		this.elo = elo;
	}

	

}
