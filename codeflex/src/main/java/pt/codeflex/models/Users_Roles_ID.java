package pt.codeflex.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Users_Roles_ID implements Serializable {

	@Column(name = "users_id")
	private Long users_id;

	@Column(name = "roles_id")
	private Long roles_id;

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}

	public Long getRoles_id() {
		return roles_id;
	}

	public void setRoles_id(Long roles_id) {
		this.roles_id = roles_id;
	}

	public Users_Roles_ID() {
	}

	public Users_Roles_ID(Long users_id, Long roles_id) {
		this.users_id = users_id;
		this.roles_id = roles_id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Users_Roles_ID))
			return false;
		Users_Roles_ID that = (Users_Roles_ID) o;
		return Objects.equals(getUsers_id(), that.getUsers_id())
				&& Objects.equals(getRoles_id(), that.getRoles_id());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUsers_id(), getRoles_id());
	}
}
