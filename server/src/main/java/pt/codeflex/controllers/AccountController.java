package pt.codeflex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.codeflex.databasemodels.Users;
import pt.codeflex.repositories.UsersRepository;

@Controller
@RequestMapping(path = "/sssaccount")
public class AccountController {

	@Autowired
	private UsersRepository userRepository;
	
	@GetMapping(path = "/login")
	public String login(Model model) {
		model.addAttribute("user", new Users());
		return "/account/login";
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String loginPOST(@ModelAttribute("user") Users user) {
		return "/all";
	}
	
	@GetMapping(path = "/register")
	public String register(Model model) {
		model.addAttribute("user", new Users());
		return "/account/register";
	}
	
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public String registerPOST(@ModelAttribute("user") Users user) {
		// TODO : validate data
		System.out.println(user.getEmail());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		userRepository.save(user);
		return "redirect:/all";
	}
}
