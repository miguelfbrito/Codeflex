package pt.codeflex.models;

import pt.codeflex.databasemodels.Tournament;
import pt.codeflex.databasemodels.Users;

public class RegisterUserOnTournament {

	private Users user;
	private Tournament tournament;
	
	public RegisterUserOnTournament() {
		
	}
	
	public RegisterUserOnTournament(Users user, Tournament tournament) {
		super();
		this.user = user;
		this.tournament = tournament;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}
	
	

	
	
}
