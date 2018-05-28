package pt.codeflex.custom;

import java.util.List;

import pt.codeflex.databasemodels.Users;

public interface CustomUsersRepository {
	List<Users> findUsersUnder30();
}
