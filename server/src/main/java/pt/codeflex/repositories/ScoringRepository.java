package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasecompositekeys.ScoringID;
import pt.codeflex.models.Scoring;
import pt.codeflex.models.Submissions;
@CrossOrigin
@Repository
public interface ScoringRepository extends JpaRepository<Scoring, ScoringID>{
	List<Scoring> findAllBySubmissions(Submissions submission);
}
