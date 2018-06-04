package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasecompositeskeys.MembersID;
import pt.codeflex.databasemodels.Members;

@Repository
public interface MembersRepository extends JpaRepository<Members, MembersID>{

}
