package pt.codeflex.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Groups {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String name;
	
	private String accessCode;
	
	@OneToMany
	private List<Members> member = new ArrayList<>();
	
	public Groups(String name, String accessCode) {
		this.name = name;
		this.accessCode = accessCode;
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

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public List<Members> getMember() {
		return member;
	}

	public void setMember(List<Members> member) {
		this.member = member;
	}


}
