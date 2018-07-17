package pt.codeflex.controllers;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import pt.codeflex.databasemodels.Role;
import pt.codeflex.databasemodels.Users;
import pt.codeflex.databasemodels.UsersRoles;
import pt.codeflex.jsonresponses.GenericResponse;
import pt.codeflex.models.UserLessInfo;
import pt.codeflex.repositories.RoleRepository;
import pt.codeflex.repositories.UsersRepository;
import pt.codeflex.repositories.UsersRolesRepository;

@RestController
@RequestMapping("/api/account")
public class UsersController {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UsersRolesRepository usersRolesRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/login")
	public GenericResponse login(@RequestBody Users user) {
		Users currentUser = usersRepository.findByUsername(user.getUsername());

		System.out.println("Logging in /api/account/login");
		System.out.println(user.getUsername());

		if (currentUser != null) {

			if (bCryptPasswordEncoder.matches(user.getPassword(), currentUser.getPassword())) {

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				System.out.println(auth.getAuthorities());

				UserLessInfo finalUser = new UserLessInfo();
				finalUser.convert(currentUser);

				return new GenericResponse(finalUser, "Logged in");
			}
		}

		return new GenericResponse(null, "Invalid username or password");
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Users user) {
		GenericResponse genericResponse = null;
		System.out.println("Registering " + user.toString());

		Users findByUsername = usersRepository.findByUsername(user.getUsername());
		Users findByEmail = usersRepository.findByEmail(user.getEmail());

		System.out.println("Fetching for possible existent accounts");
		if (findByUsername != null) {
			System.out.println(findByUsername.toString());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		if (findByEmail != null) {
			System.out.println(findByEmail.toString());
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		Users newUser = new Users(user.getUsername(), user.getEmail(),
				bCryptPasswordEncoder.encode(user.getPassword()));

		Users savedUser = usersRepository.save(newUser);

		// TEST PURPOSES
		if (savedUser.getUsername().equals("admin")) {
			addUsersRoles(new UsersRoles(savedUser, viewRoleById((long) 2)));
		} else {
			addUsersRoles(new UsersRoles(savedUser, viewRoleById((long) 1)));
		}

		// UserLessInfo finalUser = new UserLessInfo();
		// finalUser.convert(newUser);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/Users/isRegistered/{username}")
	public boolean isUserRegistered(@PathVariable String username) {
		Users user = usersRepository.findByUsername(username);
		if (user != null) {
			return true;
		}
		return false;
	}

	// ROLE

	@GetMapping(path = "/Role/view")
	public Iterable<Role> getAllRole() {
		return roleRepository.findAll();
	}

	@PostMapping(path = "/Role/add")
	public void addRole(@RequestBody Role role) {
		roleRepository.save(role);
	}

	@PostMapping(path = "/Role/edit")
	public void editRole(@RequestParam long id) {
		Optional<Role> r = roleRepository.findById(id);

		if (r.isPresent()) {
			Role role = r.get();
			roleRepository.save(role);
		}
	}

	@PostMapping(path = "/Role/delete/{id}")
	public void deleteRole(@PathVariable long id) {
		roleRepository.deleteById(id);
	}

	@GetMapping(path = "/Role/view/{id}")
	public Role viewRoleById(@PathVariable long id) {

		Optional<Role> r = roleRepository.findById(id);
		if (r.isPresent()) {
			return r.get();
		}

		return null;
	}

	// USERSROLES

	@GetMapping(path = "/UsersRoles/view")
	public List<UsersRoles> getAllUsersRoles() {
		return usersRolesRepository.findAll();
	}

	@PostMapping(path = "/UsersRoles/add")
	public UsersRoles addUsersRoles(@RequestBody UsersRoles usersRoles) {
		return usersRolesRepository.save(usersRoles);
	}

	@PostMapping(path = "/UsersRoles/edit")
	public void editUsersRoles(@RequestParam long id) {
		Optional<UsersRoles> u = usersRolesRepository.findById(id);

		if (u.isPresent()) {
			UsersRoles usersRoles = u.get();
			usersRolesRepository.save(usersRoles);
		}
	}

	@PostMapping(path = "/UsersRoles/delete/{id}")
	public void deleteUsersRoles(@PathVariable long id) {
		usersRolesRepository.deleteById(id);
	}

	@GetMapping(path = "/UsersRoles/view/{id}")
	public Optional<UsersRoles> viewUsersRolesById(@PathVariable long id) {
		return usersRolesRepository.findById(id);
	}

}
