package pt.codeflex.databasemodels;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import pt.codeflex.databasecompositeskeys.UsersRolesID;

// https://vladmihalcea.com/the-best-way-to-map-a-composite-primary-key-with-jpa-and-hibernate/

@IdClass(UsersRolesID.class)
@Entity
public class UsersRoles implements Serializable{

	@Id
	@ManyToOne
	private Users users;

	@Id
	@ManyToOne
	private Role role;


	public UsersRoles() {}
	public UsersRoles(Users users, Role role) {
		this.users= users;
		this.role = role;
	}

	public Users getUserId() {
		return users;
	}

	public void setUserId(Users userId) {
		this.users = userId;
	}

	public Role getRoleId() {
		return role;
	}

	public void setRoleId(Role roleId) {
		this.role = roleId;
	}

}
