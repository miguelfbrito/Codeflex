package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pt.codeflex.databasecompositekeys.RatingID;
import pt.codeflex.models.Rating;
import pt.codeflex.models.TestCase;
import pt.codeflex.repositories.RatingRepository;

@Service
public class RatingService extends CRUDService<Rating, RatingID> {


    private RatingRepository repository;

    @Autowired
    public RatingService(CrudRepository<Rating, RatingID> repository) {
        super(repository);
    }


}
