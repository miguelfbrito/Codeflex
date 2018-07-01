package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasemodels.Difficulty;

@CrossOrigin
@Repository
public interface DifficultyRepository extends JpaRepository<Difficulty, Long>{

}
