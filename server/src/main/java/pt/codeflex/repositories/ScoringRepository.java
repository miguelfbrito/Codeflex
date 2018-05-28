package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Scoring;

@Repository
public interface ScoringRepository extends CrudRepository<Scoring, Long>{

}
