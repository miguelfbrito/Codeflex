package pt.codeflex.databasemodels;

import java.util.Date;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import pt.codeflex.models.TournamentLeaderboard;

@Entity

@SqlResultSetMappings({
		@SqlResultSetMapping(name = "HighestScoreByUserByProblemMapping", classes = @ConstructorResult(targetClass = Leaderboard.class, columns = {
				@ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "score", type = Double.class) })),

		@SqlResultSetMapping(name = "TournamentLeaderboardMapping", classes = @ConstructorResult(targetClass = TournamentLeaderboard.class, columns = {
				@ColumnResult(name = "username", type = String.class), @ColumnResult(name = "score", type = Double.class),
				@ColumnResult(name = "openingDate", type = Date.class),
				@ColumnResult(name = "completionDate", type = Date.class) })) })

public class Leaderboard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private double score;

	@ManyToOne
	private Users user;

	@OneToOne
	private Problem problem;

	private String language;

	public Leaderboard() {

	}

	public Leaderboard(long id, double score) {
		this.id = id;
		this.score = score;
	}

	public Leaderboard(long id, double score, Users user, Problem problem) {
		this.id = id;
		this.score = score;
		this.user = user;
		this.problem = problem;
	}

	public Leaderboard(double score, Users user, Problem problem, String language) {
		this.score = score;
		this.user = user;
		this.problem = problem;
		this.language = language;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
