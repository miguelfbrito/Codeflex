package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

}
