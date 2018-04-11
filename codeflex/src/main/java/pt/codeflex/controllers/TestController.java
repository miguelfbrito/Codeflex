package pt.codeflex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.codeflex.controllers.api.DatabaseController;
import pt.codeflex.models.Users;

@Controller
public class TestController {

	@Autowired
	private DatabaseController databaseController;
	
	@GetMapping("/test")
	public @ResponseBody String test() {
		Users u = databaseController.addUsers("Test Username", "test@gmail.com", "5102510fosdf!#");
		return u.getUsername();
	}
}
