package pt.codeflex.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

// TODO : implement hashCode and equals methods
@Embeddable
public class MembersID implements Serializable{
	
	private long group;
	private long user;
	
	public MembersID(long groups, long users) {
		this.group = groups;
		this.user= users;
	}

	public long getGroups() {
		return group;
	}

	public void setGroups(long groups) {
		this.group = groups;
	}

	public long getUsers() {
		return user;
	}

	public void setUsers(long users) {
		this.user = users;
	}
}
