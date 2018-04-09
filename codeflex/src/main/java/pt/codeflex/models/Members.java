package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@IdClass(MembersID.class)
@Entity
public class Members implements Serializable {

	@Id
	@ManyToOne
	private Groups group;
	
	@Id 
	@ManyToOne
	private Users user;

	public Members() {}
	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	
}
