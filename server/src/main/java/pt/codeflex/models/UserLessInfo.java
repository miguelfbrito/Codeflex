package pt.codeflex.models;

import pt.codeflex.databasemodels.Users;

public class UserLessInfo {

	private long id;
	private String username;
	private double rating;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public UserLessInfo() {}
	public UserLessInfo(long id, String username , double rating) {
		super();
		this.id = id;
		this.username = username;
		this.rating = rating;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void convert(Users user) {
		this.id = user.getId();
		this.username = user.getUsername();
	}
	
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
}
