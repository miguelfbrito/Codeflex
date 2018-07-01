package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasemodels.TestCases;

@CrossOrigin
@Repository
public interface TestCasesRepository extends JpaRepository<TestCases, Long>{

}
