package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import pt.codeflex.databasemodels.UsersRoles;

public interface UsersRolesRepository extends JpaRepository<UsersRoles, Long> {

}
