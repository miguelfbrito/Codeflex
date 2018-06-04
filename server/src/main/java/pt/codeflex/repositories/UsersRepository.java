package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Users;

//@Transactional
@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	List<Users> findAllByEmail(String email);
	Users findByUsername(String username);
	Users findByEmail(String email);

}
