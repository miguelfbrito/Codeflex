package pt.codeflex.custom;

import java.util.List;

import pt.codeflex.models.Users;

public interface CustomUsersRepository {
	List<Users> findUsersUnder30();
}
