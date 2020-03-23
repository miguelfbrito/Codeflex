package pt.codeflex.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Groups {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String name;
	
	private String password;
	
	@OneToMany
	@JoinColumn(name = "groups_id")
	private List<Members> member = new ArrayList<>();
	
	public Groups(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Members> getMember() {
		return member;
	}

	public void setMember(List<Members> member) {
		this.member = member;
	}


}
