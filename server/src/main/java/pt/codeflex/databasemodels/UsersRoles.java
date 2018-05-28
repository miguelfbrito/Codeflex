package pt.codeflex.databasemodels;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// https://vladmihalcea.com/the-best-way-to-map-a-composite-primary-key-with-jpa-and-hibernate/

@IdClass(UsersRolesID.class)
@Entity
public class UsersRoles implements Serializable{

	@Id
	@ManyToOne
	private Users user;

	@Id
	@ManyToOne
	private Role role;


	public UsersRoles() {}
	public UsersRoles(Users user, Role role) {
		this.user = user;
		this.role = role;
	}

	public Users getUserId() {
		return user;
	}

	public void setUserId(Users userId) {
		this.user = userId;
	}

	public Role getRoleId() {
		return role;
	}

	public void setRoleId(Role roleId) {
		this.role = roleId;
	}

}
