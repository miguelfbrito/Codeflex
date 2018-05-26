package pt.codeflex.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.models.Users;
import pt.codeflex.custom.CustomUsersRepository;

//@Transactional
@Repository
public interface UsersRepository extends CrudRepository<Users, Long>, CustomUsersRepository{
	List<Users> findByEmail(String email);

}
