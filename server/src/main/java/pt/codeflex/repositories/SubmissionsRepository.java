package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.custom.CustomSubmissionsRepository;
import pt.codeflex.databasemodels.Submissions;

@Repository
public interface SubmissionsRepository extends CrudRepository<Submissions, Long>, CustomSubmissionsRepository{

}
