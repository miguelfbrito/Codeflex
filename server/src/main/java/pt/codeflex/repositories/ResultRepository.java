package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long>{

}
