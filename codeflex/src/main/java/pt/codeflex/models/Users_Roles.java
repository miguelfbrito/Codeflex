package pt.codeflex.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

// https://vladmihalcea.com/the-best-way-to-map-a-composite-primary-key-with-jpa-and-hibernate/

@Entity
public class Users_Roles {

	@EmbeddedId
	private Users_Roles_ID id;
	
}
