package pt.codeflex.models;

import java.util.List;


public interface ListCategoriesWithStatsInterface {

	List<ListCategoriesWithStats> getAllByUserId(long userId);
	
}
