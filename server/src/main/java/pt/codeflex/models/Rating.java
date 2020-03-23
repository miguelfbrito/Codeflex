package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import pt.codeflex.databasecompositekeys.RatingID;

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
	
	public Rating() {}
	
	public Rating(Tournament tournament, Users users, double elo) {
		super();
		this.tournament = tournament;
		this.users = users;
		this.elo = elo;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public double getElo() {
		return elo;
	}

	public void setElo(double elo) {
		this.elo = elo;
	}

	

}
