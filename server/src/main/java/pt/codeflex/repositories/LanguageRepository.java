package pt.codeflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import pt.codeflex.databasemodels.Language;
@CrossOrigin
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long>{
	Language findByName(String name);
}
