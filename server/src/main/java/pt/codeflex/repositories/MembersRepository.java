package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasecompositeskeys.MembersID;
import pt.codeflex.databasemodels.Members;
@CrossOrigin
@Repository
public interface MembersRepository extends JpaRepository<Members, MembersID>{

}
