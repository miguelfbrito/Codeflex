package pt.codeflex.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.core.net.SyslogOutputStream;
import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.databasemodels.TestCases;
import pt.codeflex.databasemodels.Users;
import pt.codeflex.evaluatesubmissions.EvaluateSubmissions;
import pt.codeflex.models.Host;
import pt.codeflex.models.ListCategoriesWithStats;
import pt.codeflex.repositories.PractiseCategoryRepository;
import pt.codeflex.repositories.RatingRepository;
import pt.codeflex.repositories.TournamentRepository;

@Controller
public class TestController {

}
