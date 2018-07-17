package pt.codeflex.databasemodels;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Users {

	// TODO: add validation for fields

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String username;

	@Column(unique = true)
	private String email;

	private String password;

	private Date registrationDate;

	private double globalRating;

	public void setId(long id) {
		this.id = id;
	}

	public Users() {
	}

	public Users(String username) {
		this.username = username;
	}

	public Users(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.registrationDate = Calendar.getInstance().getTime();
		this.globalRating = (double) 1500;

		MessageDigest digest = null;

		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		String encoded = Base64.getEncoder().encodeToString(hash);
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

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public double getGlobalRating() {
		return globalRating;
	}

	public void setGlobalRating(double globalRating) {
		this.globalRating = globalRating;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", registrationDate=" + registrationDate + ", globalRating=" + globalRating + "]";
	}

}
