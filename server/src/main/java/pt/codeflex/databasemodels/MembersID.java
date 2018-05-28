package pt.codeflex.databasemodels;

import java.io.Serializable;

import javax.persistence.Embeddable;

// TODO : implement hashCode and equals methods
@Embeddable
public class MembersID implements Serializable{
	
	private long groups;
	private long users;
	
	public MembersID(long groups, long users) {
		this.groups = groups;
		this.users= users;
	}

	public long getGroups() {
		return groups;
	}

	public void setGroups(long groups) {
		this.groups = groups;
	}

	public long getUsers() {
		return users;
	}

	public void setUsers(long users) {
		this.users = users;
	}
}
