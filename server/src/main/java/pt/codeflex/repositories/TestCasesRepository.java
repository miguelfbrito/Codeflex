package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.TestCases;


@Repository
public interface TestCasesRepository extends CrudRepository<TestCases, Long>{

}
