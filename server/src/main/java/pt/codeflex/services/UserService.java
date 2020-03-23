package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pt.codeflex.models.Problem;
import pt.codeflex.models.Users;
import pt.codeflex.repositories.UsersRepository;

import java.util.Optional;

@Service
public class UserService extends CRUDService<Users, Long> {


    private UsersRepository usersRepository;

    @Autowired
    public UserService(CrudRepository<Users, Long> repository) {
        super(repository);
    }

    public Users getById(long id) {
        Optional<Users> byId = usersRepository.findById(id);
        return byId.orElse(null);
    }

    public Users getByName(String name) {
        return usersRepository.findByUsername(name);
    }


}
