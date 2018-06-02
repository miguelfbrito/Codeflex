package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.codeflex.databasemodels.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long>{
	Language findByName(String name);
}
