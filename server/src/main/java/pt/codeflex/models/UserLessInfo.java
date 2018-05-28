package pt.codeflex.models;

import pt.codeflex.databasemodels.Users;

public class UserLessInfo {

	private long id;
	private String username;
	private String email;
	
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
	public UserLessInfo(long id, String username, String email) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void convert(Users user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
