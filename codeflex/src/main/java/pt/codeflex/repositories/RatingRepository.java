package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.models.Rating;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long>{

}
