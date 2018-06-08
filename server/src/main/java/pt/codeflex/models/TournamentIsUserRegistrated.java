package pt.codeflex.models;

import pt.codeflex.databasemodels.Tournament;

public class TournamentIsUserRegistrated {

	private Tournament tournament;
	private boolean isRegistered;
	
	public TournamentIsUserRegistrated(Tournament tournament, boolean isRegistered) {
		super();
		this.tournament = tournament;
		this.isRegistered = isRegistered;
	}
	
	public Tournament getTournament() {
		return tournament;
	}
	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}
	public boolean isRegistered() {
		return isRegistered;
	}
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

}
