package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import pt.codeflex.databasecompositekeys.MembersID;

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

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	
}
