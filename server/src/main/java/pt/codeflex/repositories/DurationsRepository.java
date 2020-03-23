package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasecompositekeys.DurationsID;
import pt.codeflex.models.Durations;
@CrossOrigin
@Repository
public interface DurationsRepository extends JpaRepository<Durations, DurationsID>{

}
