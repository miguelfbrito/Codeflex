package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasemodels.UsersRoles;
@CrossOrigin
public interface UsersRolesRepository extends JpaRepository<UsersRoles, Long> {

}
