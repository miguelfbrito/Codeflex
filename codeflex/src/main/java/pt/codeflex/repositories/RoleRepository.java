package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.models.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{

}
