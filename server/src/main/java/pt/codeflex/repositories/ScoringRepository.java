package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Scoring;
import pt.codeflex.databasemodels.Submissions;

@Repository
public interface ScoringRepository extends JpaRepository<Scoring, Long>{
	List<Scoring> findAllBySubmissions(Submissions submission);
}
