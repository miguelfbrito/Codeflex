package pt.codeflex.custom;

import java.util.List;

public interface CustomTournamentRepository {

	double findScoreOfUserInTournament (long userId, long tournamentId);
}
