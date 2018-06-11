package pt.codeflex.utils;

import java.util.ArrayList;
import java.util.List;

import pt.codeflex.databasemodels.Users;

public class RatingCalculator {

	private static final int K = 32; // set constant used for calculating ratings

	public static List<Users> updateRatingTournament(List<Users> usersInTournament) {

		List<Users> usersUpdated = new ArrayList<>();

		for (int i = 0; i < usersUpdated.size(); i++) {

			Users currentUser = usersUpdated.get(i);
			double sumExpectedRating = 0;
			double sumPoints = 0;

			for (int j = 0; j < usersUpdated.size(); j++) {
				Users opponent = usersUpdated.get(j);
				sumExpectedRating += expectedRating(currentUser.getGlobalRating(), opponent.getGlobalRating());
			//	sumPoints += pointsComparasion(, ratingB)
			
			// score of a user on a tournament needs to be fetched
				
			}
		}

		return usersUpdated;
	}

	public static double expectedRating(double ratingA, double ratingB) {
		return 1 / (1 + Math.pow(10, (ratingB - ratingA) / 400));
	}

	public static double pointsComparasion(double ratingA, double ratingB) {
		if (ratingA > ratingB) {
			return 1;
		} else if (ratingA < ratingB) {
			return 0;
		}
		return 0.5;
	}

}
