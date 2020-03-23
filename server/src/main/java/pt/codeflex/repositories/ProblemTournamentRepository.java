package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasecompositekeys.ProblemTournamentID;
import pt.codeflex.models.ProblemTournament;
@CrossOrigin
@Repository
public interface ProblemTournamentRepository extends JpaRepository<ProblemTournament, ProblemTournamentID>{

}
