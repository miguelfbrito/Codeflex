package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{

}
