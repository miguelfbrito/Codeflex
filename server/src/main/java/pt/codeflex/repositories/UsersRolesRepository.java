package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.models.UsersRoles;
@CrossOrigin
public interface UsersRolesRepository extends JpaRepository<UsersRoles, Long> {

}
