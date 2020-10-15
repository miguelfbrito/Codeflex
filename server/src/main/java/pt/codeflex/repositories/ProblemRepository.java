package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.models.Problem;
import pt.codeflex.models.Tournament;
import pt.codeflex.models.Users;
@CrossOrigin
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
	Problem findByName(String name);
	List<Problem> findAllByTournament(Tournament tournament);
	List<Problem> findAllByOwner(Users owner);
}
