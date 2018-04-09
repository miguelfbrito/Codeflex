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

import pt.codeflex.models.*;

import pt.codeflex.repositories.*;

@Controller
@RequestMapping(path = "/api/database")
public class DatabaseController {

	@Autowired
	private GroupsRepository groupsRepository;

	@Autowired
	private LeaderboardRepository leaderboardRepository;

	@Autowired
	private MembersRepository membersRepository;

	@Autowired
	private PractiseCategoryRepository practiseCategoryRepository;

	@Autowired
	private ProblemRepository problemRepository;

	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ScoringRepository scoringRepository;

	@Autowired
	private SubmissionsRepository submissionsRepository;

	@Autowired
	private TestCasesRepository testCasesRepository;

	@Autowired
	private TournamentRepository tournamentRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UsersRolesRepository usersRolesRepository;

	// GROUPS

	@GetMapping(path = "/Groups/view")
	public @ResponseBody Iterable<Groups> getAllGroups() {
		return groupsRepository.findAll();
	}

	@PostMapping(path = "/Groups/add")
	public void addGroups() {
	}

	@PostMapping(path = "/Groups/delete/{id}")
	public void deleteGroups(@PathVariable long id) {
		groupsRepository.deleteById(id);
	}

	@GetMapping(path = "/Groups/view/{id}")
	public @ResponseBody Optional<Groups> viewGroupsById(@PathVariable long id) {
		return groupsRepository.findById(id);
	}

	// LEADERBOARD

	@GetMapping(path = "/Leaderboard/view")
	public @ResponseBody Iterable<Leaderboard> getAllLeaderboard() {
		return leaderboardRepository.findAll();
	}

	@PostMapping(path = "/Leaderboard/add")
	public void addLeaderboard() {
	}

	@PostMapping(path = "/Leaderboard/delete/{id}")
	public void deleteLeaderboard(@PathVariable long id) {
		leaderboardRepository.deleteById(id);
	}

	@GetMapping(path = "/Leaderboard/view/{id}")
	public @ResponseBody Optional<Leaderboard> viewLeaderboardById(@PathVariable long id) {
		return leaderboardRepository.findById(id);
	}

	// MEMBERS

	@GetMapping(path = "/Members/view")
	public @ResponseBody Iterable<Members> getAllMembers() {
		return membersRepository.findAll();
	}

	@PostMapping(path = "/Members/add")
	public void addMembers() {
	}

	@PostMapping(path = "/Members/delete/{id}")
	public void deleteMembers(@PathVariable long id) {
		membersRepository.deleteById(id);
	}

	@GetMapping(path = "/Members/view/{id}")
	public @ResponseBody Optional<Members> viewMembersById(@PathVariable long id) {
		return membersRepository.findById(id);
	}

	// PRACTISECATEGORY

	@GetMapping(path = "/PractiseCategory/view")
	public @ResponseBody Iterable<PractiseCategory> getAllPractiseCategory() {
		return practiseCategoryRepository.findAll();
	}

	@PostMapping(path = "/PractiseCategory/add")
	public void addPractiseCategory() {
	}

	@PostMapping(path = "/PractiseCategory/delete/{id}")
	public void deletePractiseCategory(@PathVariable long id) {
		practiseCategoryRepository.deleteById(id);
	}

	@GetMapping(path = "/PractiseCategory/view/{id}")
	public @ResponseBody Optional<PractiseCategory> viewPractiseCategoryById(@PathVariable long id) {
		return practiseCategoryRepository.findById(id);
	}

	// PROBLEM

	@GetMapping(path = "/Problem/view")
	public @ResponseBody Iterable<Problem> getAllProblem() {
		return problemRepository.findAll();
	}

	@PostMapping(path = "/Problem/add")
	public void addProblem() {
	}

	@PostMapping(path = "/Problem/delete/{id}")
	public void deleteProblem(@PathVariable long id) {
		problemRepository.deleteById(id);
	}

	@GetMapping(path = "/Problem/view/{id}")
	public @ResponseBody Optional<Problem> viewProblemById(@PathVariable long id) {
		return problemRepository.findById(id);
	}

	// RATING

	@GetMapping(path = "/Rating/view")
	public @ResponseBody Iterable<Rating> getAllRating() {
		return ratingRepository.findAll();
	}

	@PostMapping(path = "/Rating/add")
	public void addRating() {
	}

	@PostMapping(path = "/Rating/delete/{id}")
	public void deleteRating(@PathVariable long id) {
		ratingRepository.deleteById(id);
	}

	@GetMapping(path = "/Rating/view/{id}")
	public @ResponseBody Optional<Rating> viewRatingById(@PathVariable long id) {
		return ratingRepository.findById(id);
	}

	// ROLE

	@GetMapping(path = "/Role/view")
	public @ResponseBody Iterable<Role> getAllRole() {
		return roleRepository.findAll();
	}

	@PostMapping(path = "/Role/add")
	public void addRole() {
	}

	@PostMapping(path = "/Role/delete/{id}")
	public void deleteRole(@PathVariable long id) {
		roleRepository.deleteById(id);
	}

	@GetMapping(path = "/Role/view/{id}")
	public @ResponseBody Optional<Role> viewRoleById(@PathVariable long id) {
		return roleRepository.findById(id);
	}

	// SCORING

	@GetMapping(path = "/Scoring/view")
	public @ResponseBody Iterable<Scoring> getAllScoring() {
		return scoringRepository.findAll();
	}

	@PostMapping(path = "/Scoring/add")
	public void addScoring() {
	}

	@PostMapping(path = "/Scoring/delete/{id}")
	public void deleteScoring(@PathVariable long id) {
		scoringRepository.deleteById(id);
	}

	@GetMapping(path = "/Scoring/view/{id}")
	public @ResponseBody Optional<Scoring> viewScoringById(@PathVariable long id) {
		return scoringRepository.findById(id);
	}

	// SUBMISSIONS

	@GetMapping(path = "/Submissions/view")
	public @ResponseBody Iterable<Submissions> getAllSubmissions() {
		return submissionsRepository.findAll();
	}

	@PostMapping(path = "/Submissions/add")
	public void addSubmissions() {
	}

	@PostMapping(path = "/Submissions/delete/{id}")
	public void deleteSubmissions(@PathVariable long id) {
		submissionsRepository.deleteById(id);
	}

	@GetMapping(path = "/Submissions/view/{id}")
	public @ResponseBody Optional<Submissions> viewSubmissionsById(@PathVariable long id) {
		return submissionsRepository.findById(id);
	}

	// TESTCASES

	@GetMapping(path = "/TestCases/view")
	public @ResponseBody Iterable<TestCases> getAllTestCases() {
		return testCasesRepository.findAll();
	}

	@PostMapping(path = "/TestCases/add")
	public void addTestCases() {
	}

	@PostMapping(path = "/TestCases/delete/{id}")
	public void deleteTestCases(@PathVariable long id) {
		testCasesRepository.deleteById(id);
	}

	@GetMapping(path = "/TestCases/view/{id}")
	public @ResponseBody Optional<TestCases> viewTestCasesById(@PathVariable long id) {
		return testCasesRepository.findById(id);
	}

	// TOURNAMENT

	@GetMapping(path = "/Tournament/view")
	public @ResponseBody Iterable<Tournament> getAllTournament() {
		return tournamentRepository.findAll();
	}

	@PostMapping(path = "/Tournament/add")
	public void addTournament() {
	}

	@PostMapping(path = "/Tournament/delete/{id}")
	public void deleteTournament(@PathVariable long id) {
		tournamentRepository.deleteById(id);
	}

	@GetMapping(path = "/Tournament/view/{id}")
	public @ResponseBody Optional<Tournament> viewTournamentById(@PathVariable long id) {
		return tournamentRepository.findById(id);
	}

	// USERS

	@GetMapping(path = "/Users/view")
	public @ResponseBody Iterable<Users> getAllUsers() {
		return usersRepository.findAll();
	}

	@PostMapping(path = "/Users/add")
	public void addUsers(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
		Users user = new Users(username, email, password);
		usersRepository.save(user);
	}

	@PostMapping(path = "/Users/delete/{id}")
	public void deleteUsers(@PathVariable long id) {
		usersRepository.deleteById(id);
	}

	@GetMapping(path = "/Users/view/{id}")
	public @ResponseBody Optional<Users> viewUsersById(@PathVariable long id) {
		return usersRepository.findById(id);
	}

	// USERSROLES

	@GetMapping(path = "/UsersRoles/view")
	public @ResponseBody Iterable<UsersRoles> getAllUsersRoles() {
		return usersRolesRepository.findAll();
	}

	@PostMapping(path = "/UsersRoles/add")
	public void addUsersRoles() {
	}

	@PostMapping(path = "/UsersRoles/delete/{id}")
	public void deleteUsersRoles(@PathVariable long id) {
		usersRolesRepository.deleteById(id);
	}

	@GetMapping(path = "/UsersRoles/view/{id}")
	public @ResponseBody Optional<UsersRoles> viewUsersRolesById(@PathVariable long id) {
		return usersRolesRepository.findById(id);
	}

}
