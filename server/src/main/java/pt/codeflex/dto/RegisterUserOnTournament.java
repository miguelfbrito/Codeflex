package pt.codeflex.dto;

import pt.codeflex.models.Tournament;
import pt.codeflex.models.Users;

public class RegisterUserOnTournament {

	private Users users;
	private Tournament tournament;
	
	public RegisterUserOnTournament() {
		
	}
	
	public RegisterUserOnTournament(Users users, Tournament tournament) {
		super();
		this.users = users;
		this.tournament = tournament;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	@Override
	public String toString() {
		return "RegisterUserOnTournament [user=" + users + ", tournament=" + tournament + "]";
	}
	
	
}
