package pt.codeflex.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.models.PractiseCategory;


@Repository
public interface PractiseCategoryRepository extends CrudRepository<PractiseCategory, Long>{

}
