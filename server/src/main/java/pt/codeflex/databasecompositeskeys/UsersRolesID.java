package pt.codeflex.databasecompositeskeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class UsersRolesID implements Serializable {

	private long users;
	private long role;
	
	public UsersRolesID() {}
	
	public UsersRolesID(long userId, long roleId) {
		this.users = userId;
		this.role = roleId;
	}
	
	public long getUserId() {
		return users;
	}
	public void setUserId(long userId) {
		this.users = userId;
	}
	public long getRoleId() {
		return role;
	}
	public void setRoleId(long roleId) {
		this.role = roleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (role ^ (role >>> 32));
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
		UsersRolesID other = (UsersRolesID) obj;
		if (role != other.role)
			return false;
		if (users != other.users)
			return false;
		return true;
	}

	
}
