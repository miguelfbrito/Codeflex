package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.custom.CustomRatingRepository;
import pt.codeflex.databasecompositeskeys.RatingID;
import pt.codeflex.databasemodels.Rating;
import pt.codeflex.databasemodels.Tournament;
import pt.codeflex.databasemodels.Users;
@CrossOrigin
@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingID>, CustomRatingRepository {

	List<Rating> findByTournament(Tournament tournament);

}
