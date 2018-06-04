package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.TestCases;


@Repository
public interface TestCasesRepository extends JpaRepository<TestCases, Long>{

}
