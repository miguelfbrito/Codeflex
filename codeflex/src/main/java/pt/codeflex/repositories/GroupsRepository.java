package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.models.Groups;

@Repository
public interface GroupsRepository extends CrudRepository<Groups, Long>{

}
