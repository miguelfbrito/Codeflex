package pt.codeflex.custom;

import java.util.List;

import pt.codeflex.databasemodels.Submissions;

public interface CustomSubmissionsRepository {
	List<Submissions> findSubmissionsToAvaliate();
}
