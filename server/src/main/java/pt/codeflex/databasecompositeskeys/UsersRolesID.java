package pt.codeflex.databasecompositeskeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class UsersRolesID implements Serializable {

	private long user;
	private long role;
	
	public UsersRolesID() {}
	
	public UsersRolesID(long userId, long roleId) {
		this.user = userId;
		this.role = roleId;
	}
	
	public long getUserId() {
		return user;
	}
	public void setUserId(long userId) {
		this.user = userId;
	}
	public long getRoleId() {
		return role;
	}
	public void setRoleId(long roleId) {
		this.role = roleId;
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (!(o instanceof Users_Roles_ID))
//			return false;
//		Users_Roles_ID that = (Users_Roles_ID) o;
//		return Objects.equals(getUsers_id(), that.getUsers_id())
//				&& Objects.equals(getRoles_id(), that.getRoles_id());
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(getUsers_id(), getRoles_id());
//	}
	
}
