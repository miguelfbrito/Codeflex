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

@Controller
public class TestController {

	@Autowired
	private DatabaseController databaseController;

	@GetMapping("/test")
	public @ResponseBody String test() {
		Iterable<Submissions> s = databaseController.getAllSubmissions();
		for (Submissions su : s) {
			System.out.println(su.getId());
			System.out.println(su.getCode());
			System.out.println(su.getLanguage());
			List<TestCases> tc = su.getProblem().getTestCases();
			for(TestCases t : tc) {
				System.out.println(t.getId());
				System.out.println(t.getInput());
				System.out.println(t.getOutput());
			}
			System.out.println(su.getDate());
			System.out.println(" --- ");

		}
		return "";
	}

}
