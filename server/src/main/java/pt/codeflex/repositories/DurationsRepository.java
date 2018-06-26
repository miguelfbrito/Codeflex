package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasecompositeskeys.DurationsID;
import pt.codeflex.databasemodels.Durations;

@Repository
public interface DurationsRepository extends JpaRepository<Durations, DurationsID>{

}
