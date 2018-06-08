package pt.codeflex.models;

import java.util.List;

public class TournamentsToList {

	private List<TournamentIsUserRegistrated> availableTournaments;
	private List<TournamentIsUserRegistrated> archivedTournaments;

	
	public TournamentsToList() {}
	
	public TournamentsToList(List<TournamentIsUserRegistrated> availableTournaments,
			List<TournamentIsUserRegistrated> archivedTournaments) {
		super();
		this.availableTournaments = availableTournaments;
		this.archivedTournaments = archivedTournaments;
	}
	public List<TournamentIsUserRegistrated> getAvailableTournaments() {
		return availableTournaments;
	}
	public void setAvailableTournaments(List<TournamentIsUserRegistrated> availableTournaments) {
		this.availableTournaments = availableTournaments;
	}
	public List<TournamentIsUserRegistrated> getArchivedTournaments() {
		return archivedTournaments;
	}
	public void setArchivedTournaments(List<TournamentIsUserRegistrated> archivedTournaments) {
		this.archivedTournaments = archivedTournaments;
	}
	
	
}