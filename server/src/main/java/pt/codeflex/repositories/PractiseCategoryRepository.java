package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.custom.CustomPractiseCategoryRepository;
import pt.codeflex.databasemodels.PractiseCategory;


@Repository
public interface PractiseCategoryRepository extends JpaRepository<PractiseCategory, Long>, CustomPractiseCategoryRepository{
	PractiseCategory findByName(String name);
}
