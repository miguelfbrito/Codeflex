package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.custom.CustomTournamentRepository;
import pt.codeflex.models.Tournament;
import pt.codeflex.models.Users;
@CrossOrigin
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>, CustomTournamentRepository {
	Tournament findByName(String name);
	Tournament findByCode(String code);
	Tournament deleteByName(String name);
	List<Tournament> findByOwner(Users owner);
}
