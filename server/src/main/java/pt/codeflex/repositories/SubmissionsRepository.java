package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.custom.CustomSubmissionsRepository;
import pt.codeflex.models.Problem;
import pt.codeflex.models.Submissions;

@CrossOrigin
@Repository
public interface SubmissionsRepository extends JpaRepository<Submissions, Long>, CustomSubmissionsRepository{
	List<Submissions> findAllByProblem(Problem problem);
}
