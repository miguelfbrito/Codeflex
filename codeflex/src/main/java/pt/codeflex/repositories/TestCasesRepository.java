package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.models.TestCases;


@Repository
public interface TestCasesRepository extends CrudRepository<TestCases, Long>{

}
