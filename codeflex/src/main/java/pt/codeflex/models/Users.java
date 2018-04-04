package pt.codeflex.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Users {

	// TODO: add validation for fields

	@Id
	@SequenceGenerator(name = "seq_users_id", sequenceName = "seq_users_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_users_id")
	private long id;
	private String username;
	private String email;
	private String password;
	private int age;
	
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


}
