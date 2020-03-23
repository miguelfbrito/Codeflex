package pt.codeflex.dto;

import java.util.List;

import pt.codeflex.models.Tournament;
import pt.codeflex.models.Users;

public class TournamentWithRegisteredUsers {

	private Tournament tournament;
	private List<Users> users;

	public TournamentWithRegisteredUsers(Tournament tournament, List<Users> users) {
		super();
		this.tournament = tournament;
		this.users = users;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

}
