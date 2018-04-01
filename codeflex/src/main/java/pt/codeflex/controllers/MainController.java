package pt.codeflex.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.codeflex.models.Role;
import pt.codeflex.models.Users;
import pt.codeflex.models.Users_Roles;
import pt.codeflex.models.Users_Roles_ID;
import pt.codeflex.repositories.RoleRepository;
import pt.codeflex.repositories.UsersRepository;
import pt.codeflex.repositories.UsersRolesRepository;

@Controller
public class MainController {

	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UsersRolesRepository usersRolesRepository;
	
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
		return "User saved";
	}

	@GetMapping(path = "add/user_roles")
	public @ResponseBody String addNewUserRole(@RequestParam long userId, @RequestParam long roleId) {
		Optional<Users> u = userRepository.findById(userId);
		if(!u.isPresent()) {
			return "User doesn't exist";
		}
		Optional<Role> r = roleRepository.findById(roleId);
		if(!r.isPresent()) {
			return "Role doesn't exist";
		}
		
		Users_Roles ur = new Users_Roles(userId, roleId);
		usersRolesRepository.save(ur);
		return "Users_Roles saved!"; 
	}
	
	@GetMapping(path = "/add/roles")
	public @ResponseBody String addNewRole(@RequestParam String name) {
		Role r = new Role(name);
		roleRepository.save(r);
		return "Role saved";

	}
	
	@GetMapping(path = "/view/users")
	public @ResponseBody Iterable<Users> getAllUsers() {
		return userRepository.findAll(); 
	}

	@GetMapping(path = "/view/roles")
	public @ResponseBody Iterable<Role> getAllRoles() {
		return roleRepository.findAll(); 
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
