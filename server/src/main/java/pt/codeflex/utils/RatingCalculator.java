package pt.codeflex.utils;

public class RatingCalculator {

	public static final int K = 32; // set constant used for calculating ratings. Impacts the rating variability

	// Equation to calculate expected value
	public static double expectedRating(double ratingA, double ratingB) {
		return 1 / (1 + Math.pow(10, (ratingB - ratingA) / 400));
	}

	// Return points based on which player has the highest score
	public static double pointsComparasion(double ratingA, double ratingB) {
		if (ratingA > ratingB) {
			return 1;
		} else if (ratingA < ratingB) {
			return 0;
		}
		return 0.5;
	}

}
