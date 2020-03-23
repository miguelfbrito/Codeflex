package pt.codeflex.databasecompositekeys;

import java.io.Serializable;

import javax.persistence.Embeddable;

// TODO : implement hashCode and equals methods
@Embeddable
public class MembersID implements Serializable{
	
	private long groups;
	private long users;

	public MembersID() {
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (groups ^ (groups >>> 32));
		result = prime * result + (int) (users ^ (users >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MembersID other = (MembersID) obj;
		if (groups != other.groups)
			return false;
		if (users != other.users)
			return false;
		return true;
	}
}
