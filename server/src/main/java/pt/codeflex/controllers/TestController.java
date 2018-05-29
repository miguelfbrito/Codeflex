package pt.codeflex.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.core.net.SyslogOutputStream;
import pt.codeflex.controllers.api.DatabaseController;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.TestCases;
import pt.codeflex.databasemodels.Users;
import pt.codeflex.models.ListCategoriesWithStats;
import pt.codeflex.models.ListCategoriesWithStatsImpl;
import pt.codeflex.models.ListCategoriesWithStatsInterface;
import pt.codeflex.repositories.PractiseCategoryRepository;

@Controller
public class TestController {

	@Autowired
	private PractiseCategoryRepository practiseCategoryRepository;

	@GetMapping("/test")
	public @ResponseBody List<ListCategoriesWithStats> test() {
		return practiseCategoryRepository.listCategoriesWithStatsByUserId(1);
	}
	

}
