package pt.codeflex.custom;

import java.util.List;

import pt.codeflex.models.Submissions;

public interface CustomSubmissionsRepository {
	List<Submissions> findSubmissionsToAvaliate();
}
