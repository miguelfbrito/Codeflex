package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasecompositeskeys.ScoringID;
import pt.codeflex.databasemodels.Scoring;
import pt.codeflex.databasemodels.Submissions;
@CrossOrigin
@Repository
public interface ScoringRepository extends JpaRepository<Scoring, ScoringID>{
	List<Scoring> findAllBySubmissions(Submissions submission);
}
