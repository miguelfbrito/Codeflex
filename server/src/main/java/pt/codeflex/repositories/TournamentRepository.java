package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.custom.CustomTournamentRepository;
import pt.codeflex.databasemodels.Tournament;
import pt.codeflex.databasemodels.Users;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>, CustomTournamentRepository {
	Tournament findByName(String name);
	List<Tournament> findByOwner(Users owner);
}
