package pt.codeflex.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.custom.CustomSubmissionsRepository;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.Users;

@Repository
public interface SubmissionsRepository extends CrudRepository<Submissions, Long>, CustomSubmissionsRepository{
}
