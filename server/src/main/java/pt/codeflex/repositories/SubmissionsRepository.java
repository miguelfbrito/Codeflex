package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.custom.CustomSubmissionsRepository;
import pt.codeflex.databasemodels.Problem;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.Users;
@CrossOrigin
@Repository
public interface SubmissionsRepository extends JpaRepository<Submissions, Long>, CustomSubmissionsRepository{
	List<Submissions> findAllByProblem(Problem problem);
}
