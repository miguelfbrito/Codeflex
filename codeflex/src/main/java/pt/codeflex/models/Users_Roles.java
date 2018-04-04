package pt.codeflex.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// https://vladmihalcea.com/the-best-way-to-map-a-composite-primary-key-with-jpa-and-hibernate/

@Entity
public class Users_Roles {

	@EmbeddedId
	
	private Users_Roles_ID id;
	@Column(name = "name")
	private String name;

	@ManyToOne
	private Users user;
	@ManyToOne
	private Role role;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Users_Roles(Users user, Role role) {
		id.setUsers_id(user.getId());
		id.setRoles_id(role.getId());
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
