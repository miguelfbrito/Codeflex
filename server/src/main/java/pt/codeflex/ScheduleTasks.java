package pt.codeflex;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.codeflex.controllers.DatabaseController;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.Tournament;
import pt.codeflex.models.Host;
import pt.codeflex.models.SubmissionWithTestCase;
import pt.codeflex.models.TasksBeingEvaluated;

@EnableScheduling
@Component
public class ScheduleTasks {

	@Autowired
	private DatabaseController db;

	// @Scheduled(fixedDelay = 5000)
	// public void bar() {
	// System.out.println(hostList.get(0).getCpuUsage());
	// System.out.println(hostList.get(0).getMemoryUsage());
	//
	// }

	@Scheduled(fixedDelay = 60000)
	public void checksTournamentStatus() {

		// Gets all tournaments
		List<Tournament> tournaments = db.getAllTournaments();

		long currentDate = Calendar.getInstance().getTimeInMillis();

		for (Tournament t : tournaments) {
			if (!t.getOpen()) {
				if (currentDate >= t.getStartingDate().getTime() && currentDate <= t.getStartingDate().getTime()) {
					t.setOpen(true);
					db.updateTournament(t);
				}
			} else {
				if (currentDate >= t.getEndingDate().getTime()) {
					// Close tournament
					t.setOpen(false);
					db.updateTournament(t);
					db.calculateTournamentRatings(t.getId());
				}
			}
		}
	}

}
