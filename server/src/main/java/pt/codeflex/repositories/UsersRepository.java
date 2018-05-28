package pt.codeflex.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.custom.CustomUsersRepository;
import pt.codeflex.databasemodels.Users;

//@Transactional
@Repository
public interface UsersRepository extends CrudRepository<Users, Long>, CustomUsersRepository{
	List<Users> findAllByEmail(String email);
	Users findByUsername(String username);
	Users findByEmail(String email);

}
