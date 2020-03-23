package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pt.codeflex.models.Difficulty;
import pt.codeflex.models.Users;
import pt.codeflex.repositories.DifficultyRepository;
import pt.codeflex.repositories.UsersRepository;

import java.util.Optional;

@Service
public class DifficultyService extends CRUDService<Difficulty, Long> {


    private DifficultyRepository repository;

    @Autowired
    public DifficultyService(CrudRepository<Difficulty, Long> repository) {
        super(repository);
    }

    public Difficulty getById(long id) {
        Optional<Difficulty> byId = repository.findById(id);
        return byId.orElse(null);
    }


}
