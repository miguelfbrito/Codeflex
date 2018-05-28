package pt.codeflex.databasemodels;

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
	private Groups groups;
	
	@Id 
	@ManyToOne
	private Users users;

	public Members() {}
	public Groups getGroup() {
		return groups;
	}

	public void setGroup(Groups group) {
		this.groups = group;
	}

	public Users getUser() {
		return users;
	}

	public void setUser(Users user) {
		this.users = user;
	}
	
	
}
