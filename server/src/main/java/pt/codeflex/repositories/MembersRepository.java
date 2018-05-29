package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasecomposites.MembersID;
import pt.codeflex.databasemodels.Members;

@Repository
public interface MembersRepository extends CrudRepository<Members, MembersID>{

}
