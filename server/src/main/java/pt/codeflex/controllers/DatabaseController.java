package pt.codeflex.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.codeflex.databasemodels.*;
import pt.codeflex.models.CategoriesWithoutTestCases;
import pt.codeflex.models.ListCategoriesWithStats;
import pt.codeflex.models.ProblemDifficulty;
import pt.codeflex.models.ProblemWithoutTestCases;
import pt.codeflex.repositories.*;

@RestController
@CrossOrigin
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

	@Autowired
	private DifficultyRepository difficultyRepository;

	@Autowired
	private ResultRepository resultRepository;

	@Autowired
	private LanguageRepository languageRepository;

	// LANGUAGE
	@GetMapping(path = "/Language/view")
	public List<Language> getAllLanguages() {
		return languageRepository.findAll();
	}

	@PostMapping(path = "/Language/add")
	public Language addLanguage(@RequestBody Language language) {
		return languageRepository.save(new Language(language.getName(), language.getCompilerName()));
	}

	@GetMapping(path = "/Language/view/{id}")
	public Optional<Language> viewLanguageById(@PathVariable long id) {
		return languageRepository.findById(id);
	}

	// RESULT

	@GetMapping(path = "/Result/view")
	public List<Result> getAllResults() {
		return resultRepository.findAll();
	}

	@PostMapping(path = "/Result/add")
	public Result addResult(@RequestBody Result result) {
		return resultRepository.save(new Result(result.getName()));
	}

	@GetMapping(path = "/Result/view/{id}")
	public Optional<Result> viewResultById(@PathVariable long id) {
		return resultRepository.findById(id);
	}

	// DIFFICULTY

	@GetMapping(path = "/Difficulty/view")
	public Iterable<Difficulty> getAllDifficulties() {
		return difficultyRepository.findAll();
	}

	@PostMapping(path = "/Difficulty/add")
	public Difficulty addDifficulty(@RequestBody Difficulty difficulty) {
		return difficultyRepository.save(new Difficulty(difficulty.getName()));
	}

	@GetMapping(path = "/Difficulty/view/{id}")
	public Optional<Difficulty> viewDifficultyById(@PathVariable long id) {
		return difficultyRepository.findById(id);
	}

	// GROUPS

	@GetMapping(path = "/Groups/view")
	public Iterable<Groups> getAllGroups() {
		return groupsRepository.findAll();
	}

	@PostMapping(path = "/Groups/add")
	public void addGroups() {
	}

	@PostMapping(path = "/Groups/edit")
	public void editGroups(@RequestParam long id) {
		Optional<Groups> g = groupsRepository.findById(id);

		if (g.isPresent()) {
			Groups groups = g.get();
			groupsRepository.save(groups);
		}
	}

	@PostMapping(path = "/Groups/delete/{id}")
	public void deleteGroups(@PathVariable long id) {
		groupsRepository.deleteById(id);
	}

	@GetMapping(path = "/Groups/view/{id}")
	public Optional<Groups> viewGroupsById(@PathVariable long id) {
		return groupsRepository.findById(id);
	}

	// LEADERBOARD

	@GetMapping(path = "/Leaderboard/view")
	public Iterable<Leaderboard> getAllLeaderboard() {
		return leaderboardRepository.findAll();
	}

	@PostMapping(path = "/Leaderboard/add")
	public void addLeaderboard() {
	}

	@PostMapping(path = "/Leaderboard/edit")
	public void editLeaderboard(@RequestParam long id) {
		Optional<Leaderboard> l = leaderboardRepository.findById(id);

		if (l.isPresent()) {
			Leaderboard leaderboard = l.get();
			leaderboardRepository.save(leaderboard);
		}
	}

	@PostMapping(path = "/Leaderboard/delete/{id}")
	public void deleteLeaderboard(@PathVariable long id) {
		leaderboardRepository.deleteById(id);
	}

	@GetMapping(path = "/Leaderboard/view/{id}")
	public Optional<Leaderboard> viewLeaderboardById(@PathVariable long id) {
		return leaderboardRepository.findById(id);
	}

	// MEMBERS

	@GetMapping(path = "/Members/view")
	public Iterable<Members> getAllMembers() {
		return membersRepository.findAll();
	}

	@PostMapping(path = "/Members/add")
	public void addMembers() {
	}

	// @PostMapping(path = "/Members/edit")
	// public void editMembers(@RequestParam long id) {
	// Optional<Members> m = membersRepository.findById(id);
	//
	// if (m.isPresent()) {
	// Members members = m.get();
	// membersRepository.save(members);
	// }
	// }
	//
	// @PostMapping(path = "/Members/delete/{id}")
	// public void deleteMembers(@PathVariable long id) {
	// membersRepository.deleteById(id);
	// }
	//
	// @GetMapping(path = "/Members/view/{id}")
	// public Optional<Members> viewMembersById(@PathVariable long id)
	// {
	// return membersRepository.findById(id);
	// }

	// PRACTISECATEGORY

	@GetMapping(path = "/PractiseCategory/view")
	public List<PractiseCategory> getAllPractiseCategory() {
		return (List<PractiseCategory>) practiseCategoryRepository.findAll();
	}

	@PostMapping(path = "/PractiseCategory/add")
	public PractiseCategory addPractiseCategory(@RequestParam String name) {
		PractiseCategory pc = new PractiseCategory(name);
		return practiseCategoryRepository.save(pc);
	}

	@PostMapping(path = "/PractiseCategory/edit")
	public void editPractiseCategory(@RequestParam long id, @RequestParam long problemId) {
		Optional<PractiseCategory> pc = practiseCategoryRepository.findById(id);
		Optional<Problem> p = problemRepository.findById(problemId);
		if (pc.isPresent()) {
			PractiseCategory practiseCategory = pc.get();

			if (p.isPresent()) {
				Problem problem = p.get();
				practiseCategory.getProblem().add(problem);
			}

			practiseCategoryRepository.save(practiseCategory);
		}
	}

	@PostMapping(path = "/PractiseCategory/delete/{id}")
	public void deletePractiseCategory(@PathVariable long id) {
		practiseCategoryRepository.deleteById(id);
	}

	@GetMapping(path = "/PractiseCategory/view/{id}")
	public PractiseCategory viewPractiseCategoryById(@PathVariable long id) {
		Optional<PractiseCategory> practiseCategory = practiseCategoryRepository.findById(id);
		if (practiseCategory.isPresent()) {
			return practiseCategory.get();
		}
		return new PractiseCategory();
	}

	@GetMapping(path = "/PractiseCategory/listWithStats/{id}")
	public List<ListCategoriesWithStats> listCategoriesWithStats(@PathVariable long id) {
		List<PractiseCategory> allCategories = getAllPractiseCategory();

		Optional<Users> u = usersRepository.findById(id);

		List<ListCategoriesWithStats> listOfCategoriesWithStats = null;
		if (u.isPresent()) {
			listOfCategoriesWithStats = practiseCategoryRepository.listCategoriesWithStatsByUserId(u.get().getId());
		}

		List<String> categoriesName = new ArrayList<>();

		for (ListCategoriesWithStats l : listOfCategoriesWithStats) {
			categoriesName.add(l.getName());
		}

		for (PractiseCategory pc : allCategories) {
			if (!categoriesName.contains(pc.getName()) && pc.getProblem().size() > 0) {
				listOfCategoriesWithStats
						.add(new ListCategoriesWithStats(pc.getId(), pc.getName(), 0, pc.getProblem().size()));
			}
		}

		return listOfCategoriesWithStats;
	}

	@GetMapping(path = "/PractiseCategory/getAllWithoutTestCases/{userId}")
	public List<CategoriesWithoutTestCases> getAllCategoriesWithoutTestCases(@PathVariable long userId) {
		List<PractiseCategory> allCategories = getAllPractiseCategory();
		List<CategoriesWithoutTestCases> categoriesWithoutTestCases = new ArrayList<>();

		if (allCategories != null) {
			for (PractiseCategory pc : allCategories) {
				List<Problem> problems = pc.getProblem();
				List<ProblemWithoutTestCases> problemsWithoutTestCases = new ArrayList<>();
				if (problems != null) {
					for (Problem p : problems) {
						List<Submissions> submissions = getAllSubmissionsByUserIdAndProblemId(userId, p.getId());
						boolean solved = false;
						for (Submissions s : submissions) {
							Result result = s.getResult();
							if (result != null && result.getName().equals("Correct")) {
								solved = true;
							}
						}
						problemsWithoutTestCases.add(new ProblemWithoutTestCases(p.getId(), p.getName(),
								p.getDescription(), p.getDifficulty(), solved));
					}
				}
				categoriesWithoutTestCases
						.add(new CategoriesWithoutTestCases(pc.getId(), pc.getName(), problemsWithoutTestCases));
			}
		}

		return categoriesWithoutTestCases;
	}

	@GetMapping(path = "/PractiseCategory/getAllProblemsByCategoryName/{categoryName}")
	public List<ProblemWithoutTestCases> getAllProblemsByCategoryId(@PathVariable String categoryName) {
		categoryName = categoryName.replace("-", " ");

		PractiseCategory categoryData = practiseCategoryRepository.findByName(categoryName);
		List<ProblemWithoutTestCases> problemWithoutTestCases = new ArrayList<>();

		if (categoryData != null) {
			List<Problem> problems = categoryData.getProblem();

			for (Problem p : problems) {
				problemWithoutTestCases.add(
						new ProblemWithoutTestCases(p.getId(), p.getName(), p.getDescription(), p.getDifficulty()));
			}
		}

		return problemWithoutTestCases;
	}

	// PROBLEM

	@GetMapping(path = "/Problem/view")
	public Iterable<Problem> getAllProblem() {
		return problemRepository.findAll();
	}

	@PostMapping(path = "/Problem/add")
	public Problem addProblem(@RequestBody ProblemDifficulty problemDifficulty) {

		Optional<Difficulty> f = difficultyRepository.findById(problemDifficulty.getDifficulty().getId());

		if (f.isPresent()) {
			Problem p = problemDifficulty.getProblem();
			p.setDifficulty(f.get());
			return problemRepository.save(p);
		}

		return null;
	}

	@PostMapping(path = "/Problem/addTestCase")
	public void addTestCasesToProblem(@RequestParam long problemId, @RequestParam long testCaseId) {
		Optional<TestCases> tc = testCasesRepository.findById(testCaseId);
		Optional<Problem> p = problemRepository.findById(problemId);

		if (p.isPresent() && tc.isPresent()) {
			Problem problem = p.get();
			problem.getTestCases().add(tc.get());
			problemRepository.save(problem);
		}
	}

	@PostMapping(path = "/Problem/edit")
	public void editProblem(@RequestParam long id) {
		Optional<Problem> p = problemRepository.findById(id);

		if (p.isPresent()) {
			Problem problem = p.get();
			problemRepository.save(problem);
		}
	}

	@PostMapping(path = "/Problem/delete/{id}")
	public void deleteProblem(@PathVariable long id) {
		problemRepository.deleteById(id);
	}

	@GetMapping(path = "/Problem/view/{id}")
	public Optional<Problem> viewProblemById(@PathVariable long id) {
		return problemRepository.findById(id);
	}

	@GetMapping(path = "/Problem/getProblemByName/{problemName}")
	public Problem getProblemByName(@PathVariable String problemName) {
		problemName = problemName.replace("-", " ");
		Problem p = problemRepository.findByName(problemName);
		if (p != null) {
			return p;
		}
		return new Problem();
	}

	// RATING

	@GetMapping(path = "/Rating/view")
	public Iterable<Rating> getAllRating() {
		return ratingRepository.findAll();
	}

	@PostMapping(path = "/Rating/add")
	public void addRating() {
	}

	@PostMapping(path = "/Rating/edit")
	public void editRating(@RequestParam long id) {
		Optional<Rating> r = ratingRepository.findById(id);

		if (r.isPresent()) {
			Rating rating = r.get();
			ratingRepository.save(rating);
		}
	}

	@PostMapping(path = "/Rating/delete/{id}")
	public void deleteRating(@PathVariable long id) {
		ratingRepository.deleteById(id);
	}

	@GetMapping(path = "/Rating/view/{id}")
	public Optional<Rating> viewRatingById(@PathVariable long id) {
		return ratingRepository.findById(id);
	}

	// ROLE

	@GetMapping(path = "/Role/view")
	public Iterable<Role> getAllRole() {
		return roleRepository.findAll();
	}

	@PostMapping(path = "/Role/add")
	public void addRole() {
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
	public Optional<Role> viewRoleById(@PathVariable long id) {
		return roleRepository.findById(id);
	}

	// SCORING

	@GetMapping(path = "/Scoring/view")
	public Iterable<Scoring> getAllScoring() {
		return scoringRepository.findAll();
	}

	@PostMapping(path = "/Scoring/add")
	public void addScoring(@RequestParam long submissionsId, @RequestParam long testCasesId, @RequestParam double value,
			@RequestParam boolean isRight) {
		Optional<Submissions> s = submissionsRepository.findById(submissionsId);
		Optional<TestCases> tc = testCasesRepository.findById(testCasesId);

		// TODO : alterar de boolean para int puro
		int tmp = 1;
		if(!isRight) {
			tmp = 0;
		}
		
		if (s.isPresent() && tc.isPresent()) {
			Scoring sc = new Scoring(s.get(), tc.get(), value, tmp);
			scoringRepository.save(sc);
		}
	}

	@PostMapping(path = "/Scoring/edit")
	public void editScoring(@RequestParam long id) {
		Optional<Scoring> s = scoringRepository.findById(id);

		if (s.isPresent()) {
			Scoring scoring = s.get();
			scoringRepository.save(scoring);
		}
	}

	@PostMapping(path = "/Scoring/delete/{id}")
	public void deleteScoring(@PathVariable long id) {
		scoringRepository.deleteById(id);
	}

	@GetMapping(path = "/Scoring/view/{id}")
	public Optional<Scoring> viewScoringById(@PathVariable long id) {
		return scoringRepository.findById(id);
	}

	@GetMapping(path = "/Scoring/viewBySubmissionId/{submissionId}")
	public List<Scoring> viewBySubmissionId(@PathVariable long submissionId) {

		Optional<Submissions> submission = submissionsRepository.findById(submissionId);
		List<Scoring> listScoring = new ArrayList<>();
		if (submission.isPresent()) {
			listScoring = scoringRepository.findAllBySubmissions(submission.get());
		}
		return listScoring;
	}
	// SUBMISSIONS

	@GetMapping(path = "/Submissions/view")
	public List<Submissions> getAllSubmissions() {
		return (List<Submissions>) submissionsRepository.findAll();
	}

	@GetMapping(path = "/Submissions/viewByUserId/{userId}")
	public List<Submissions> getAllSubmissionsByUserId(@PathVariable long userId) {
		Optional<Users> user = usersRepository.findById(userId);
		return user.get().getSubmissions();
	}

	@GetMapping(path = "/Submissions/viewByUserIdAndProblemId/{userId}/{problemId}")
	public List<Submissions> getAllSubmissionsByUserIdAndProblemId(@PathVariable long userId,
			@PathVariable long problemId) {
		List<Submissions> submissions = getAllSubmissionsByUserId(userId);
		Optional<Problem> problem = problemRepository.findById(problemId);

		List<Submissions> finalList = new ArrayList<>();
		if (problem.isPresent()) {
			for (Submissions s : submissions) {
				if (s.getProblem() == problem.get()) {
					finalList.add(s);
				}
			}
		}

		return finalList;
	}

	@PostMapping(path = "/Submissions/add")
	public void addSubmissions(@RequestParam long usersId, @RequestParam long problemId, @RequestParam String language,
			@RequestParam String code) throws ParseException {

		Optional<Users> u = usersRepository.findById(usersId);
		Optional<Problem> p = problemRepository.findById(problemId);

		if (u.isPresent()) {
			Users user = u.get();
			Problem problem;
			if (p.isPresent()) {
				problem = p.get();
				Language l = languageRepository.findByName(language);

				Submissions s = new Submissions(problem, l, code);
				user.getSubmissions().add(s);
				submissionsRepository.save(s);
				usersRepository.save(user);
			}
		}

	}

	@PostMapping(path = "/Submissions/edit")
	public void editSubmissions(@RequestParam long id) {
		Optional<Submissions> s = submissionsRepository.findById(id);

		if (s.isPresent()) {
			Submissions submissions = s.get();
			submissionsRepository.save(submissions);
		}
	}

	@PostMapping(path = "/Submissions/delete/{id}")
	public void deleteSubmissions(@PathVariable long id) {
		submissionsRepository.deleteById(id);
	}

	@GetMapping(path = "/Submissions/view/{id}")
	public Optional<Submissions> viewSubmissionsById(@PathVariable long id) {
		return submissionsRepository.findById(id);
	}

	// TESTCASES

	@GetMapping(path = "/TestCases/view")
	public Iterable<TestCases> getAllTestCases() {
		return testCasesRepository.findAll();
	}

	@PostMapping(path = "/TestCases/add")
	public void addTestCases(@RequestParam String input, @RequestParam String output) {
		TestCases tc = new TestCases(input, output);
		testCasesRepository.save(tc);
	}

	@PostMapping(path = "/TestCases/edit")
	public void editTestCases(@RequestParam long id) {
		Optional<TestCases> t = testCasesRepository.findById(id);

		if (t.isPresent()) {
			TestCases testCases = t.get();
			testCasesRepository.save(testCases);
		}
	}

	@PostMapping(path = "/TestCases/delete/{id}")
	public void deleteTestCases(@PathVariable long id) {
		testCasesRepository.deleteById(id);
	}

	@GetMapping(path = "/TestCases/view/{id}")
	public Optional<TestCases> viewTestCasesById(@PathVariable long id) {
		return testCasesRepository.findById(id);
	}

	// TOURNAMENT

	@GetMapping(path = "/Tournament/view")
	public Iterable<Tournament> getAllTournament() {
		return tournamentRepository.findAll();
	}

	@PostMapping(path = "/Tournament/add")
	public Tournament addTournament(@RequestParam String name, @RequestParam String startingDate,
			@RequestParam String description, @RequestParam int duration) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date d = sdf.parse(startingDate);
		Tournament t = new Tournament(name, d, description, duration);
		return tournamentRepository.save(t);
	}

	// TODO : refactor if needed
	// @PostMapping(path = "/Tournament/edit")
	// public void editTournament(@RequestParam long id, @RequestParam long
	// problemId) {
	// Optional<Tournament> t = tournamentRepository.findById(id);
	// Optional<Problem> p = problemRepository.findById(problemId);
	//
	// if (t.isPresent()) {
	// Tournament tournament = t.get();
	//
	// if (p.isPresent()) {
	// Problem problem = p.get();
	// tournament.getProblems().add(problem);
	// }
	// tournamentRepository.save(tournament);
	// }
	// }

	@PostMapping(path = "/Tournament/delete/{id}")
	public void deleteTournament(@PathVariable long id) {
		tournamentRepository.deleteById(id);
	}

	@GetMapping(path = "/Tournament/view/{id}")
	public Optional<Tournament> viewTournamentById(@PathVariable long id) {
		return tournamentRepository.findById(id);
	}

	// USERS

	@GetMapping(path = "/Users/view")
	public List<Users> getAllUsers() {
		return (List<Users>) usersRepository.findAll();
	}

	@PostMapping(path = "/Users/add")
	public Users addUsers(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
		Users u = new Users(username, email, password);
		return usersRepository.save(u);
	}

	@PostMapping(path = "/Users/edit")
	public void editUsers(@RequestParam long id) {
		Optional<Users> u = usersRepository.findById(id);

		if (u.isPresent()) {
			Users users = u.get();
			usersRepository.save(users);
		}
	}

	@PostMapping(path = "/Users/delete/{id}")
	public void deleteUsers(@PathVariable long id) {
		usersRepository.deleteById(id);
	}

	@GetMapping(path = "/Users/view/{id}")
	public Optional<Users> viewUsersById(@PathVariable long id) {
		return usersRepository.findById(id);
	}

	// USERSROLES

	@GetMapping(path = "/UsersRoles/view")
	public Iterable<UsersRoles> getAllUsersRoles() {
		return usersRolesRepository.findAll();
	}

	@PostMapping(path = "/UsersRoles/add")
	public void addUsersRoles() {
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
