package pt.codeflex.databasecompositekeys;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class RatingID implements Serializable {

	private long tournament;
	private long users;
	
	public RatingID() {}

	public RatingID(long tournament, long users) {
		super();
		this.tournament = tournament;
		this.users = users;
	}

	public long getTournament() {
		return tournament;
	}

	public void setTournament(long tournament) {
		this.tournament = tournament;
	}

	public long getUsers() {
		return users;
	}

	public void setUsers(long users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (tournament ^ (tournament >>> 32));
		result = prime * result + (int) (users ^ (users >>> 32));
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
		RatingID other = (RatingID) obj;
		if (tournament != other.tournament)
			return false;
		if (users != other.users)
			return false;
		return true;
	}

	

}
