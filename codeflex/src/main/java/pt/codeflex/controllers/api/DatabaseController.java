package pt.codeflex.controllers.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.codeflex.models.Problem;
import pt.codeflex.models.Role;
import pt.codeflex.models.Submissions;
import pt.codeflex.models.TestCases;
import pt.codeflex.models.Users;
import pt.codeflex.models.Users_Roles;
import pt.codeflex.models.Users_Roles_ID;
import pt.codeflex.repositories.ProblemRepository;
import pt.codeflex.repositories.RoleRepository;
import pt.codeflex.repositories.SubmissionsRepository;
import pt.codeflex.repositories.TestCasesRepository;
import pt.codeflex.repositories.UsersRepository;
import pt.codeflex.repositories.UsersRolesRepository;

@Controller
@RequestMapping(path = "/api/database")
public class DatabaseController {

	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UsersRolesRepository usersRolesRepository;

	@Autowired
	private ProblemRepository problemRepository;

	@Autowired
	private SubmissionsRepository submissionsRepository;

	@Autowired
	private TestCasesRepository testCasesRepository;

	@GetMapping(path = "/add/user")
	public @ResponseBody String addNewUser(@RequestParam String username, @RequestParam String email,
			@RequestParam int age) {

		Users n = new Users();
		n.setUsername(username);
		n.setEmail(email);
		n.setAge(age);
		userRepository.save(n);
		return "User saved";
	}

	@GetMapping(path = "/add/users_roles")
	public @ResponseBody String addNewUserRole(@RequestParam long userId, @RequestParam long roleId) {
		Optional<Users> u = userRepository.findById(userId);
		if (!u.isPresent()) {
			return "User doesn't exist";
		}
		Optional<Role> r = roleRepository.findById(roleId);
		if (!r.isPresent()) {
			return "Role doesn't exist";
		}

		Users user = u.get();
		Role role = r.get();
		System.out.println("INFORMATION: " + user.getUsername() + " " + role.getName());
		Users_Roles ur = new Users_Roles(user, role);
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

	@GetMapping(path = "/view/users_roles")
	public @ResponseBody Iterable<Users_Roles> getAllUsersRoles() {
		return usersRolesRepository.findAll();
	}

	// SUBMISSIONS

	@GetMapping(path = "/view/submissions")
	public @ResponseBody Iterable<Submissions> viewSubmissions() {
		return submissionsRepository.findAll();
	}

	@PostMapping(path = "/add/submissions")
	public @ResponseBody String addSubmissionsPOST(@RequestParam long userId, @RequestParam long problemId,
			@RequestParam String date, @RequestParam String language, @RequestParam String code) throws ParseException {
		Optional<Users> u = userRepository.findById(userId);
		Optional<Problem> p = problemRepository.findById(problemId);

		if (!u.isPresent() && !p.isPresent()) {
			return "Invalid info!";
		}

		Users user = u.get();
		Problem problem = p.get();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date d = sdf.parse(date);
		Submissions s = new Submissions(problem, user, d, language, code);
		submissionsRepository.save(s);
		return "Submission saved!";
	}

	// PROBLEMS

	@PostMapping(path = "/add/problem")
	public @ResponseBody String addProblemPost(@RequestParam String name, @RequestParam String description)
			throws ParseException {
		Problem p = new Problem(name, description);
		problemRepository.save(p);
		return "Problem saved!";
	}

	@GetMapping(path = "/view/problems")
	public @ResponseBody Iterable<Problem> viewProblems() {
		return problemRepository.findAll();
	}

	// TEST CASES

	@PostMapping(path = "/add/testcases")
	public @ResponseBody String addTestCasesPOST(@RequestParam long problemId, @RequestParam String input,
			@RequestParam String output) throws ParseException {
		Optional<Problem> p = problemRepository.findById(problemId);
		if (!p.isPresent()) {
			return "Invalid info";
		}
		Problem problem = p.get();
		TestCases tc = new TestCases(input, output);
		problem.getTestCases().add(tc);
		testCasesRepository.save(tc);
		problemRepository.save(problem);
		return "Problem saved!";
	}

	@GetMapping(path = "/view/testcases")
	public @ResponseBody Iterable<TestCases> viewTestCases() {
		return testCasesRepository.findAll();
	}

}
