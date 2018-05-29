package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Difficulty;

@Repository
public interface DifficultyRepository extends JpaRepository<Difficulty, Long>{

}
