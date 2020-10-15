package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.custom.CustomRatingRepository;
import pt.codeflex.databasecompositekeys.RatingID;
import pt.codeflex.models.Rating;
import pt.codeflex.models.Tournament;

@CrossOrigin
@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingID>, CustomRatingRepository {

	List<Rating> findByTournament(Tournament tournament);

}
