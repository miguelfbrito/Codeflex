package pt.codeflex.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

// https://vladmihalcea.com/the-best-way-to-map-a-composite-primary-key-with-jpa-and-hibernate/

@Entity
public class Users_Roles {

	@EmbeddedId
	private Users_Roles_ID id;
	@Column(name = "name")
	private String name;

	
	private Users user;
	private Role role;
	
	
	public Users_Roles(long usersId, long rolesId) {
		id.setUsers_id(usersId);
		id.setRoles_id(rolesId);
	}
	
	public Users_Roles_ID getId() {
		return id;
	}

	public void setId(Users_Roles_ID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
