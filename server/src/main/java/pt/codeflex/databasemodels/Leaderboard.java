package pt.codeflex.databasemodels;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;

import pt.codeflex.models.ListCategoriesWithStats;

@SqlResultSetMapping(name = "HighestScoreByUserByProblemMapping", classes = @ConstructorResult(targetClass = Leaderboard.class, columns = {
	    @ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "score", type = Double.class)}))

@Entity
public class Leaderboard {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private double score;

	@ManyToOne
	private Users user;
	
	@OneToOne
	private Problem problem;
	
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
	public Leaderboard(double score, Users user, Problem problem) {
		this.score = score;
		this.user = user;
		this.problem = problem;
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
}
