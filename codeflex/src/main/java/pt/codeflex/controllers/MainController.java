package pt.codeflex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.codeflex.models.Users;
import pt.codeflex.repositories.UsersRepository;

@Controller
public class MainController {

	@Autowired
	private UsersRepository userRepository;


	@GetMapping(path = "/test")
	public String test() {
		return "test";
	}
	
	@GetMapping(path = "/add")
	public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email,
			@RequestParam int age) {

		Users n = new Users();
		n.setUsername(name);
		n.setEmail(email);
		n.setAge(age);
		userRepository.save(n);
		return "Saved";
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Users> getAllUsers() {
		return userRepository.findAll(); // Returns JSON
	}

	@GetMapping(path = "/under30")
	public @ResponseBody Iterable<Users> getUsersUnder30() {
		return userRepository.findUsersUnder30();
	}
	
	@GetMapping(path = "/test1")
	@ResponseBody
	public String test1() {
		return "on test1";
	}


}
