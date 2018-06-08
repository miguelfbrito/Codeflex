package pt.codeflex.custom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import pt.codeflex.databasemodels.Rating;
import pt.codeflex.repositories.RatingRepository;

public class CustomRatingRepositoryImpl implements CustomRatingRepository{

//	@Autowired
//	private RatingRepository ratingRepository;
//	  @PersistenceContext
//	   private EntityManager em;
//	
//	@Override
//	public double getEloByTournamentIdByUserId(long tournamentId, long userId) {
//		
//		List<Rating> allRatings = ratingRepository.findAll();
//		for(Rating r : allRatings) {
//			if(r.getTournament().getId() == tournamentId && r.getUser().getId() == userId) {
//				return r.getElo();
//			}
//		}
//		
//		return 0;
//	}

}
