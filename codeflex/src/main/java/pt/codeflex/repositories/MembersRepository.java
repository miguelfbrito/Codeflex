package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.models.Members;

@Repository
public interface MembersRepository extends CrudRepository<Members, Long>{

}
