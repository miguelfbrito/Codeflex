package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasemodels.Result;
@CrossOrigin
@Repository
public interface ResultRepository extends JpaRepository<Result, Long>{
	Result findByName(String name);
	List<Result> findAllByName(String name);
}
