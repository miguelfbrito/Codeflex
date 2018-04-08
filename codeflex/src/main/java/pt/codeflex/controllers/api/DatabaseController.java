package pt.codeflex.controllers.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.codeflex.models.Problem;
import pt.codeflex.models.Role;
import pt.codeflex.models.Submissions;
import pt.codeflex.models.TestCases;
import pt.codeflex.models.Tournament;
import pt.codeflex.models.Users;
import pt.codeflex.models.UsersRoles;
import pt.codeflex.models.UsersRolesID;
import pt.codeflex.repositories.ProblemRepository;
import pt.codeflex.repositories.RoleRepository;
import pt.codeflex.repositories.SubmissionsRepository;
import pt.codeflex.repositories.TestCasesRepository;
import pt.codeflex.repositories.TournamentRepository;
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

	@Autowired
	private TournamentRepository tournamentRepository;

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
		UsersRoles ur = new UsersRoles(user, role);
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
	public @ResponseBody Iterable<UsersRoles> getAllUsersRoles() {
		return usersRolesRepository.findAll();
	}

	// SUBMISSIONS

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
		Submissions s = new Submissions(d, language, code);
		user.getSubmissions().add(s);
		submissionsRepository.save(s);

		problem.getSubmissions().add(s);
		problemRepository.save(problem);
		userRepository.save(user);

		return "Submission saved!";
	}

	@PostMapping(path = "/delete/submissions/{id}")
	public @ResponseBody String deleteSubmission(@PathVariable long id) {
		submissionsRepository.deleteById(id);
		return "Submission removed";
	}

	@GetMapping(path = "/view/submissions")
	public @ResponseBody Iterable<Submissions> viewSubmissions() {
		return submissionsRepository.findAll();

	}

	@GetMapping(path = "/view/submissions/{id}")
	public @ResponseBody Optional<Submissions> viewSubmissionsById(@PathVariable long id) {
		return submissionsRepository.findById(id);
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

	@PostMapping(path = "/delete/problem")
	public @ResponseBody String deleteProblem(@RequestParam long id) {
		if (problemRepository.existsById(id)) {
			problemRepository.deleteById(id);
			return "Problem removed";
		} else {
			return "Problem not found";
		}
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

	@PostMapping(path = "/delete/testcases/{id}")
	public @ResponseBody String deleteTestCase(@PathVariable long id) {
		Optional<TestCases> t = testCasesRepository.findById(id);
		TestCases tc = t.get();
		testCasesRepository.delete(tc);
		return "Test Case removed";
	}

	// TOURNAMENT

	@PostMapping(path = "/add/tournament")
	public @ResponseBody String addTournamentPOST(@RequestParam long problemId, @RequestParam String name,
			@RequestParam String description, @RequestParam int duration) throws ParseException {
		Tournament t = new Tournament(name, description, duration);
		Optional<Problem> p = problemRepository.findById(problemId);
		if (!p.isPresent())
			return "Invalid info!";
		Problem problem = p.get();

		t.getProblems().add(problem);
		tournamentRepository.save(t);
		return "Saved tournament!";
	}

	@GetMapping(path = "/view/tournaments")
	public @ResponseBody Iterable<Tournament> viewTournaments() {
		return tournamentRepository.findAll();
	}

}
