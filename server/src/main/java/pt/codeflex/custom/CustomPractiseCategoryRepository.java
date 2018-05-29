package pt.codeflex.custom;

import java.util.List;

import pt.codeflex.models.ListCategoriesWithStats;

public interface CustomPractiseCategoryRepository {
	List<ListCategoriesWithStats> listCategoriesWithStatsByUserId(long userId);

}
