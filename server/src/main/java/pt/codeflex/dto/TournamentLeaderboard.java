package pt.codeflex.dto;

import java.util.Date;

public class TournamentLeaderboard {

	private String username;
	private double score;
	private Date openingDate;
	private Date completionDate;
	private long totalMilliseconds;
	
	public TournamentLeaderboard(String username, double score, Date openingDate, Date completionDate) {
		super();
		this.username = username;
		this.score = score;
		this.openingDate = openingDate;
		this.completionDate = completionDate;
	}

	public TournamentLeaderboard(String username, double score, Date openingDate, Date completionDate, long totalMilliseconds) {
		super();
		this.username = username;
		this.score = score;
		this.openingDate = openingDate;
		this.completionDate = completionDate;
		this.totalMilliseconds = totalMilliseconds;
	}
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getTotalMilliseconds() {
		return totalMilliseconds;
	}

	public void setTotalMilliseconds(long totalMilliseconds) {
		this.totalMilliseconds = totalMilliseconds;
	}

}
