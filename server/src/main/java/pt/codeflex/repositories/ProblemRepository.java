package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Tournament;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
	Problem findByName(String name);
	List<Problem> findAllByTournament(Tournament tournament);
}
