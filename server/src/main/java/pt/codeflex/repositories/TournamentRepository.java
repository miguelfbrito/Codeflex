package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.models.Tournament;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Long> {

}