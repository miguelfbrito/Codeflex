package pt.codeflex.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Users {

	// TODO: add validation for fields

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String username;

	@Column(unique = true)
	private String email;

	@JsonIgnore
	private String password;
	private int age;

	@OneToMany
	@JoinColumn(name = "users_id")
	private List<Submissions> submissions = new ArrayList<Submissions>();

	@OneToMany
	private List<UsersRoles> userRoles = new ArrayList<UsersRoles>();
	
	public void setId(long id) {
		this.id = id;
	}

	public Users() {
	}

	public Users(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Submissions> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<Submissions> submissions) {
		this.submissions = submissions;
	}

	public List<UsersRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UsersRoles> userRoles) {
		this.userRoles = userRoles;
	}


}
