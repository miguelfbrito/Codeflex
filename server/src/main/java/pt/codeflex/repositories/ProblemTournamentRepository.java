package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasecomposites.ProblemTournamentID;
import pt.codeflex.databasemodels.ProblemTournament;

@Repository
public interface ProblemTournamentRepository extends JpaRepository<ProblemTournament, ProblemTournamentID>{

}
