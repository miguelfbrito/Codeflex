package pt.codeflex.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.codeflex.databasemodels.Users;

public class CustomUsersRepositoryImpl implements CustomUsersRepository {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Users> findUsersUnder30() {
		Query query = em.createNativeQuery("select * from users where age < 30",
				Users.class);
		
		return query.getResultList();
	}

}
