package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasecompositekeys.MembersID;
import pt.codeflex.models.Members;
@CrossOrigin
@Repository
public interface MembersRepository extends JpaRepository<Members, MembersID>{

}
