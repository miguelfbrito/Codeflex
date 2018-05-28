package pt.codeflex.databasemodels;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@IdClass(RatingID.class)
@Entity
public class Rating implements Serializable {

	@Id
	@ManyToOne
	private Tournament tournament;

	@Id
	@ManyToOne
	private Users users;

	private double elo;
	
	public Rating(Tournament tournament, Users user, double elo) {
		super();
		this.tournament = tournament;
		this.users = user;
		this.elo = elo;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public Users getUser() {
		return users;
	}

	public void setUser(Users user) {
		this.users = user;
	}

	public double getElo() {
		return elo;
	}

	public void setElo(double elo) {
		this.elo = elo;
	}

	

}
