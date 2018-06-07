package pt.codeflex.models;

public class UserOnProblemLeaderboard {

	private String username;
	private double score;
	private String language;

	public UserOnProblemLeaderboard(String username, double score, String language) {
		super();
		this.username = username;
		this.score = score;
		this.language = language;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}
