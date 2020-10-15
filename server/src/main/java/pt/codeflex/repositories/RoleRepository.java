package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.models.Role;
@CrossOrigin
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
