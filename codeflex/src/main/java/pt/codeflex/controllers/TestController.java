package pt.codeflex.controllers;

import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.codeflex.controllers.api.DatabaseController;
import pt.codeflex.models.Users;

@Controller
public class TestController {
	
	@Autowired
	public DatabaseController databaseController;
	// TODO : review, is this the best way to access db?
	
	@GetMapping(path = "/test")
	public @ResponseBody String testDB() {
		databaseController.addNewUser("miguelfbrito", "miguelfbrito11@gmail.com", 999);
		return "User saved";
	}
}
