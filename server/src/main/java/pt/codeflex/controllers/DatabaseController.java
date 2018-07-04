package pt.codeflex.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import pt.codeflex.databasecompositeskeys.DurationsID;
import pt.codeflex.databasecompositeskeys.RatingID;
import pt.codeflex.databasemodels.*;
import pt.codeflex.models.CategoriesWithoutTestCases;
import pt.codeflex.models.DateStatus;
import pt.codeflex.models.ListCategoriesWithStats;
import pt.codeflex.models.AddProblem;
import pt.codeflex.models.AddTournamentToProblem;
import pt.codeflex.models.ProblemWithoutTestCases;
import pt.codeflex.models.RegisterUserOnTournament;
import pt.codeflex.models.TestCasesShown;
import pt.codeflex.models.TournamentIsUserRegistrated;
import pt.codeflex.models.TournamentLeaderboard;
import pt.codeflex.models.TournamentWithRegisteredUsers;
import pt.codeflex.models.TournamentsToList;
import pt.codeflex.models.UserLessInfo;
import pt.codeflex.models.UsersLeaderboard;
import pt.codeflex.repositories.*;
import pt.codeflex.utils.DurationCalculation;
import pt.codeflex.utils.RatingCalculator;

@RestController
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

	@Autowired
	private DurationsRepository durationsRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UsersController usersController;

	// DURATIONS

	@GetMapping("/Durations/view")
	public List<Durations> getAllDurations() {
		return durationsRepository.findAll();
	}

	@GetMapping("/Durations/viewById/{userId}/{problemId}")
	public Durations viewDurationsById(@PathVariable long userId, @PathVariable long problemId) {
		Optional<Durations> d = durationsRepository.findById(new DurationsID(userId, problemId));
		if (d.isPresent()) {
			return d.get();
		}
		return new Durations();
	}

	@PostMapping("/Durations/add")
	public Durations addDurations(@RequestBody Durations duration) {
		Users user = viewUsersByUsername(duration.getUsers().getUsername());
		Problem problem = viewProblemById(duration.getProblems().getId());

		Durations newDuration = new Durations();
		if (user != null) {
			newDuration.setUsers(user);
		}

		if (problem != null) {
			newDuration.setProblems(problem);
		}

		newDuration.setOpeningDate(duration.getOpeningDate());
		newDuration.setCompletionDate(duration.getCompletionDate());

		return durationsRepository.save(newDuration);
	}

	@PostMapping("/Durations/onProblemOpening")
	public Durations addDurationOnProblemOpening(@RequestBody Durations duration) {

		Users user = viewUsersByUsername(duration.getUsers().getUsername());
		Problem problem = viewProblemById(duration.getProblems().getId());

		Durations newDuration = new Durations();

		if (user != null && problem != null) {

			newDuration.setUsers(user);
			newDuration.setProblems(problem);

			boolean openedAlready = durationsRepository.existsById(new DurationsID(user.getId(), problem.getId()));

			if (openedAlready) {
				return new Durations();
			}

			newDuration.setOpeningDate(Calendar.getInstance().getTime());

			return durationsRepository.save(newDuration);
		}

		return newDuration;

	}

	@PostMapping("/Durations/onProblemCompletion")
	public Durations updateDurationsOnProblemCompletion(@RequestBody Durations duration) {

		Users user = viewUsersByUsername(duration.getUsers().getUsername());
		Problem problem = viewProblemById(duration.getProblems().getId());

		if (user != null && problem != null) {

			Optional<Durations> d = durationsRepository.findById(new DurationsID(user.getId(), problem.getId()));

			if (d.isPresent()) {
				Durations currentDuration = d.get();

				System.out.println(currentDuration.getCompletionDate());
				if (currentDuration.getCompletionDate() == null) {
					System.out.println("NULL");
					currentDuration.setCompletionDate(Calendar.getInstance().getTime());
					return durationsRepository.save(currentDuration);
				}
			}
		}

		return new Durations();

	}
	// LEADERBOARD

	@GetMapping(path = "/Leaderboard/view")
	public List<Leaderboard> getAllLeaderboards() {
		return leaderboardRepository.findAll();
	}

	@GetMapping(path = "/Leaderboard/viewByProblemName/{problemName}")
	public List<UsersLeaderboard> getAllLeaderboardsByProblemName(@PathVariable String problemName) {

		Problem findProblembyName = problemRepository.findByName(problemName.replace("-", " "));
		List<UsersLeaderboard> userOnLeaderboard = new ArrayList<>();

		if (findProblembyName != null) {
			List<Leaderboard> findByProblem = leaderboardRepository.findAllByProblem(findProblembyName);
			for (Leaderboard l : findByProblem) {

				Durations currentUserDuration = viewDurationsById(l.getUser().getId(), l.getProblem().getId());
				long durationMilliseconds = -1;
				if (currentUserDuration != null && currentUserDuration.getOpeningDate() != null) {
					long completionDate;
					if (currentUserDuration.getCompletionDate() == null) {
						completionDate = Calendar.getInstance().getTimeInMillis();
					} else {
						completionDate = currentUserDuration.getCompletionDate().getTime();
					}
					durationMilliseconds = completionDate - currentUserDuration.getOpeningDate().getTime();
				}
				userOnLeaderboard.add(new UsersLeaderboard(l.getUser().getUsername(), l.getScore(), l.getLanguage(),
						durationMilliseconds));
			}

		}

		return userOnLeaderboard;
	}

	@PostMapping(path = "/Leaderboard/add")
	public Leaderboard addLeaderboard(@RequestBody Leaderboard leaderboard) {
		return leaderboardRepository.save(new Leaderboard(leaderboard.getScore(), leaderboard.getUser(),
				leaderboard.getProblem(), leaderboard.getLanguage()));
	}

	@GetMapping(path = "/Leaderboard/view/{id}")
	public Optional<Leaderboard> viewLeaderboardById(@PathVariable long id) {
		return leaderboardRepository.findById(id);
	}

	// LANGUAGE

	@GetMapping(path = "/Language/view")
	public List<Language> getAllLanguages() {
		return languageRepository.findAll();
	}

	@PostMapping(path = "/Language/add")
	public Language addLanguage(@RequestBody Language language) {
		return languageRepository
				.save(new Language(language.getName(), language.getCompilerName(), language.getMode()));
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
	public List<PractiseCategory> viewAllPractiseCategories() {
		return (List<PractiseCategory>) practiseCategoryRepository.findAll();
	}

	@PostMapping(path = "/PractiseCategory/add")
	public PractiseCategory addPractiseCategory(@RequestBody PractiseCategory pc) {
		return practiseCategoryRepository.save(pc);
	}

	@PostMapping("/PractiseCategory/update")
	public PractiseCategory updatePractiseCategory(@RequestParam PractiseCategory pc) {
		PractiseCategory category = viewPractiseCategoryById(pc.getId());
		if (category != null) {
		}
		return null;
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

	@DeleteMapping(path = "/PractiseCategory/delete/{id}")
	public void deletePractiseCategory(@PathVariable long id) {
		practiseCategoryRepository.deleteById(id);
	}

	@GetMapping(path = "/PractiseCategory/view/{id}")
	public PractiseCategory viewPractiseCategoryById(@PathVariable long id) {
		Optional<PractiseCategory> practiseCategory = practiseCategoryRepository.findById(id);
		if (practiseCategory.isPresent()) {
			return practiseCategory.get();
		}
		return null;
	}

	@GetMapping(path = "/PractiseCategory/viewByProblemName/{problemName}")
	public PractiseCategory viewPractiseCategoryByProblemName(@PathVariable String problemName) {
		List<PractiseCategory> allCategories = viewAllPractiseCategories();

		Problem problem = viewProblemByName(problemName);
		if (problem != null) {
			for (PractiseCategory pc : allCategories) {
				if (pc.getProblem().contains(problem)) {
					return pc;
				}
			}

		}

		return null;
	}

	@GetMapping(path = "/PractiseCategory/listWithStats/{username}")
	public List<ListCategoriesWithStats> listCategoriesWithStats(@PathVariable String username) {
		List<PractiseCategory> allCategories = viewAllPractiseCategories();

		Users u = usersRepository.findByUsername(username);

		List<ListCategoriesWithStats> listOfCategoriesWithStats = new ArrayList<>();
		if (u != null) {
			listOfCategoriesWithStats = practiseCategoryRepository.listCategoriesWithStatsByUserId(u.getId());
		}

		List<String> categoriesName = new ArrayList<>();

		for (ListCategoriesWithStats l : listOfCategoriesWithStats) {
			categoriesName.add(l.getName());
		}

		for (PractiseCategory pc : allCategories) {
			if (!categoriesName.contains(pc.getName()) && !pc.getProblem().isEmpty()) {
				listOfCategoriesWithStats
						.add(new ListCategoriesWithStats(pc.getId(), pc.getName(), 0, pc.getProblem().size()));
			}
		}

		return listOfCategoriesWithStats;
	}

	@GetMapping(path = "/PractiseCategory/getAllWithoutTestCases/{username}")
	public List<CategoriesWithoutTestCases> getAllCategoriesWithoutTestCases(@PathVariable String username) {

		List<PractiseCategory> allCategories = viewAllPractiseCategories();
		List<CategoriesWithoutTestCases> categoriesWithoutTestCases = new ArrayList<>();

		if (allCategories != null) {
			for (PractiseCategory pc : allCategories) {
				List<Problem> problems = pc.getProblem();
				List<ProblemWithoutTestCases> problemsWithoutTestCases = new ArrayList<>();
				if (problems != null) {
					for (Problem p : problems) {
						List<Submissions> submissions = getAllSubmissionsByUsernameAndProblemId(username, p.getId());
						boolean solved = false;
						for (Submissions s : submissions) {
							Result result = s.getResult();
							if (result != null && result.getName().equals("Correct")) {
								solved = true;
							}
						}
						problemsWithoutTestCases.add(new ProblemWithoutTestCases(p.getId(), p.getName(),
								p.getDescription(), p.getDifficulty(), p.getOwner(), solved));
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
	public List<Problem> getAllProblems() {
		return problemRepository.findAll();
	}

	@GetMapping(path = "/Problem/viewByName/{name}")
	public Problem viewProblemByName(@PathVariable String name) {
		return problemRepository.findByName(name.replace("-", " "));
	}

	@GetMapping(path = "/Problem/viewAllByOwnerUsername/{username}")
	public List<Problem> viewAllByOwnerUsername(@PathVariable String username) {
		Users owner = viewUsersByUsername(username);
		if (owner != null) {
			return problemRepository.findAllByOwner(owner);

		}
		return new ArrayList<>();
	}

	@GetMapping(path = "/Problem/viewAllDetails/{problemName}")
	public AddProblem viewAllProblemDetails(@PathVariable String problemName) {
		Problem problem = viewProblemByName(problemName);

		AddProblem p = new AddProblem();
		if (problem != null) {
			p.setProblem(problem);
		}

		PractiseCategory pc = viewPractiseCategoryByProblemName(problem.getName());
		if (pc != null) {
			p.setCategory(pc);
		}

		return p;
	}

	@PostMapping(path = "/Problem/add")
	public Problem addProblem(@RequestBody AddProblem addProblem) {

		Problem p = addProblem.getProblem();

		System.out.println(addProblem.toString());

		System.out.println(p.toString());

		if (p == null) {
			return new Problem();
		}

		if (addProblem.getDifficulty() != null) {
			Optional<Difficulty> f = difficultyRepository.findById(addProblem.getDifficulty().getId());
			if (f.isPresent()) {
				p.setDifficulty(f.get());
			}
		}

		// A problem can only belong to either a Tournament or a Category
		if (addProblem.getTournament() != null && addProblem.getCategory() != null) {
			System.out.println(addProblem.getTournament());
			System.out.println(addProblem.getCategory());
			System.out.println(addProblem.getProblem().getDifficulty());
			if (addProblem.getTournament().getName() != null && addProblem.getCategory().getName() != null
					&& !addProblem.getCategory().getName().equals("")) {
				return new Problem();
			}
		}

		if (addProblem.getTournament() != null) {
			if (addProblem.getTournament().getName() != null) {
				Tournament t = viewTournamentByName(addProblem.getTournament().getName());
				if (t != null) {
					p.setTournament(t);
				}
			}
		}

		if (addProblem.getOwner() != null) {
			Optional<Users> u = usersRepository.findById(addProblem.getOwner().getId());
			if (u.isPresent()) {
				System.out.println(u.get().toString());
				p.setOwner(u.get());
			} else {
				Users user = usersRepository.findByUsername(addProblem.getOwner().getUsername());
				if (u != null) {
					p.setOwner(user);
				}
			}

		} else {
			// TODO : remove. Adding a static user for now
			Optional<Users> u = usersRepository.findById((long) 2);
			if (u.isPresent()) {
				p.setOwner(u.get());
			}
		}

		Problem savedProblem = problemRepository.save(p);

		if (addProblem.getCategory() != null) {
			PractiseCategory category = viewPractiseCategoryById(addProblem.getCategory().getId());
			if (category != null) {
				category.getProblem().add(p);
				practiseCategoryRepository.save(category);
			}
		}
		return savedProblem;
	}

	@PostMapping("/Problem/update")
	public Problem updateProblem(@RequestBody AddProblem changes) {

		Problem problemUpdate = viewProblemById(changes.getProblem().getId());
		if (problemUpdate != null) {
			Problem p = changes.getProblem();

			problemUpdate.setName(p.getName());
			problemUpdate.setDescription(p.getDescription());
			problemUpdate.setConstraints(p.getConstraints());
			problemUpdate.setInputFormat(p.getInputFormat());
			problemUpdate.setOutputFormat(p.getOutputFormat());
			problemUpdate.setMaxScore(p.getMaxScore());

			// updates difficulty
			Optional<Difficulty> d = viewDifficultyById(changes.getProblem().getDifficulty().getId());
			if (d.isPresent()) {
				problemUpdate.setDifficulty(d.get());
			}

			// updates category
			// 1. removes the problem from current category
			List<PractiseCategory> categories = viewAllPractiseCategories();
			// TODO : check if this will work
			for (PractiseCategory pc : categories) {
				if (pc.getProblem().contains(p)) {
					if (pc.getName().equals(changes.getCategory().getName())) {
						break;
					}
					pc.getProblem().remove(p);
				}
			}
			practiseCategoryRepository.saveAll(categories);

			// 2. adds the problem to the category sent
			PractiseCategory category = viewPractiseCategoryById(changes.getCategory().getId());
			if (category != null) {
				category.getProblem().add(p);
				practiseCategoryRepository.save(category);
			}

			return problemRepository.save(problemUpdate);

		}

		return new Problem();
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

	@PostMapping(path = "/Problem/addTournament")
	public Problem addTournament(@RequestBody AddTournamentToProblem info) {

		Tournament tournament = viewTournamentByName(info.getTournament().getName());

		// TODO : remove
		// tournament.setShowWebsite(true);

		Problem problem = viewProblemByName(info.getProblem().getName());
		if (tournament == null || problem == null)
			return null;

		problem.setTournament(tournament);
		return problemRepository.save(problem);
	}

	@PostMapping(path = "/Problem/edit")
	public void editProblem(@RequestParam long id) {
		Optional<Problem> p = problemRepository.findById(id);

		if (p.isPresent()) {
			Problem problem = p.get();
			problemRepository.save(problem);
		}
	}

	@GetMapping("/Problem/viewAllTestCasesByProblemName/{problemName}")
	public List<TestCasesShown> viewAllTestCasesByProblemName(@PathVariable String problemName) {
		List<TestCasesShown> finalTestCases = new ArrayList<>();
		Problem p = viewProblemByName(problemName);

		if (p != null) {
			List<TestCases> testCases = p.getTestCases();
			for (TestCases tc : testCases) {
				System.out.println("INPUT " + tc.getInput());
				System.out.println("OUTPUT " + tc.getOutput());
				finalTestCases.add(new TestCasesShown(tc.getId(), tc.getInput(), tc.getOutput(), tc.getDescription(),
						tc.isShown()));
			}
		}
		return finalTestCases;
	}

	@PostMapping(path = "/Problem/delete/{id}")
	public void deleteProblem(@PathVariable long id) {
		problemRepository.deleteById(id);
	}

	@GetMapping(path = "/Problem/view/{id}")
	public Problem viewProblemById(@PathVariable long id) {
		Optional<Problem> problem = problemRepository.findById(id);

		if (problem.isPresent()) {
			return problem.get();
		}
		return null;
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
	public List<Rating> getAllRatings() {
		return ratingRepository.findAll();
	}

	@GetMapping(path = "/Rating/viewUsersByTournamentId/{tournamentId}")
	public List<Users> viewUsersByTournamentId(@PathVariable long tournamentId) {

		List<Users> usersInTournament = new ArrayList<Users>();
		List<Rating> allRatings = getAllRatings();

		Tournament t = viewTournamentById(tournamentId);

		if (t == null) {
			return null;
		}

		for (Rating r : allRatings) {
			if (r.getTournament() == t) {
				usersInTournament.add(r.getUser());
			}
		}

		return usersInTournament;
	}

	@PostMapping(path = "/Rating/add")
	public void addRating() {
	}

	@GetMapping(path = "/Rating/isUserRegisteredInTournament/{userId}/{tournamentId}")
	public boolean isUserRegisteredInTournament(@PathVariable long userId, @PathVariable long tournamentId) {
		Optional<Rating> r = ratingRepository.findById(new RatingID(tournamentId, userId));

		if (r.isPresent()) {
			return true;
		}

		return false;
	}

	/*
	 * @PostMapping(path = "/Rating/edit") public void editRating(@RequestParam long
	 * id) { Optional<Rating> r = ratingRepository.findById(id);
	 * 
	 * if (r.isPresent()) { Rating rating = r.get(); ratingRepository.save(rating);
	 * } }
	 * 
	 * @PostMapping(path = "/Rating/delete/{id}") public void
	 * deleteRating(@PathVariable long id) { ratingRepository.deleteById(id); }
	 * 
	 * @GetMapping(path = "/Rating/view/{id}") public Optional<Rating>
	 * viewRatingById(@PathVariable long id) { return ratingRepository.findById(id);
	 * }
	 */

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
		if (!isRight) {
			tmp = 0;
		}

		if (s.isPresent() && tc.isPresent()) {
			Scoring sc = new Scoring(s.get(), tc.get(), value, tmp);
			scoringRepository.save(sc);
		}
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
		List<Submissions> submissions = submissionsRepository.findAll();
		List<Submissions> finalSubmissions = new ArrayList<>();
		if (user.isPresent() && submissions != null) {
			for (Submissions s : submissions) {
				if (s.getUsers() == user.get()) {
					finalSubmissions.add(s);
				}
			}
		}

		return finalSubmissions;
	}

	@GetMapping(path = "/Submissions/viewByUsername/{username}")
	public List<Submissions> getAllSubmissionsByUsername(@PathVariable String username) {
		Users user = usersRepository.findByUsername(username.trim());
		List<Submissions> submissions = submissionsRepository.findAll();
		List<Submissions> finalSubmissions = new ArrayList<>();
		if (user != null && submissions != null) {
			for (Submissions s : submissions) {
				if (s.getUsers() == user) {
					finalSubmissions.add(s);
				}
			}
		}

		return finalSubmissions;
	}

	@GetMapping(path = "/Submissions/viewByProblemNameByUsername/{problemName}/{username}")
	public List<Submissions> getAllSubmissionsByProblemName(@PathVariable String problemName,
			@PathVariable String username) {

		problemName = problemName.replace('-', ' ');

		Problem currentProblem = problemRepository.findByName(problemName);
		List<Submissions> finalSubmissions = new ArrayList<>();

		if (currentProblem != null) {
			List<Submissions> submissions = submissionsRepository.findAllByProblem(currentProblem);
			for (Submissions s : submissions) {
				if (s.getUsers().getUsername().equals(username)) {
					finalSubmissions.add(s);
				}
			}
		}
		return finalSubmissions;
	}

	@GetMapping(path = "/Submissions/viewByUserIdAndProblemId/{username}/{problemId}")
	public List<Submissions> getAllSubmissionsByUsernameAndProblemId(@PathVariable String username,
			@PathVariable long problemId) {
		List<Submissions> submissions = getAllSubmissionsByUsername(username);
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

		if (u.isPresent() && p.isPresent()) {
			Users user = u.get();
			Problem problem;
			if (p.isPresent()) {
				problem = p.get();
				Language lang = languageRepository.findByName(language);

				Submissions s = new Submissions(problem, lang, code, user);
				submissionsRepository.save(s);
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

	@PostMapping(path = "/TestCases/add-deprecated")
	public void addTestCasesDeprecated(@RequestParam String input, @RequestParam String output) {
		TestCases tc = new TestCases(input, output);
		testCasesRepository.save(tc);
	}

	@PostMapping(path = "/TestCases/add")
	public TestCases addTestCases(@RequestBody TestCasesShown t) {
		TestCases tc = new TestCases(t.getInput(), t.getOutput(), t.getDescription(), t.isShown());
		return testCasesRepository.save(tc);
	}

	@PostMapping("/TestCases/addToProblem/{problemName}")
	public void addTestCasesToProblem(@RequestBody TestCasesShown t, @PathVariable String problemName) {

		TestCases testCase = addTestCases(t);

		if (testCase == null) {
			return;
		}

		Problem problem = viewProblemByName(problemName);

		if (problem == null) {
			return;
		}

		problem.getTestCases().add(testCase);
		problemRepository.save(problem);

	}

	@PostMapping(path = "/TestCases/edit")
	public void editTestCases(@RequestParam long id) {
		Optional<TestCases> t = testCasesRepository.findById(id);

		if (t.isPresent()) {
			TestCases testCases = t.get();
			testCasesRepository.save(testCases);
		}
	}

	@DeleteMapping(path = "/TestCases/delete/{id}")
	public void deleteTestCases(@PathVariable long id) {
		testCasesRepository.deleteById(id);
	}

	@GetMapping(path = "/TestCases/view/{id}")
	public Optional<TestCases> viewTestCasesById(@PathVariable long id) {
		return testCasesRepository.findById(id);
	}

	@PostMapping("/TestCases/update")
	public TestCases updateTestCase(@RequestBody TestCasesShown testCaseReq) {
		Optional<TestCases> tc = viewTestCasesById(testCaseReq.getId());
		if (tc.isPresent()) {
			TestCases testCase = tc.get();

			testCase.setInput(testCaseReq.getInput());
			testCase.setOutput(testCaseReq.getOutput());
			testCase.setDescription(testCaseReq.getDescription());
			testCase.setShown(testCaseReq.isShown());

			return testCasesRepository.save(testCase);
		}
		return new TestCases();
	}

	@PostMapping(path = "/TestCases/updateList")
	public void updateListOfTestCases(@RequestBody List<TestCasesShown> testCases) {
		for (TestCasesShown tc : testCases) {
			updateTestCase(tc);
		}
	}

	// TOURNAMENT

	@GetMapping(path = "/Tournament/view")
	public List<Tournament> getAllTournaments() {
		return tournamentRepository.findAll();
	}

	@GetMapping(path = "/Tournament/viewByName/{name}")
	public Tournament viewTournamentByName(@PathVariable String name) {
		return tournamentRepository.findByName(name.replace("-", " "));
	}

	@GetMapping("/Tournament/viewCurrentLeaderboard/{tournamentName}")
	public UsersLeaderboard viewCurrentLeaderboard(@PathVariable String tournamentName) {

		Tournament tournament = viewTournamentByName(tournamentName);

		if (tournament == null)
			return null;

		List<Users> usersInTournament = viewUsersByTournamentId(tournament.getId());

		List<Problem> problemsInTournament = getAllProblemsByTournamentName(tournament.getName());

		for (Users u : usersInTournament) {

		}

		return null;
	}

	@GetMapping("/Tournament/viewTournamentLeaderboard/{tournamentName}")
	public List<TournamentLeaderboard> viewTournamentLeaderboard(@PathVariable String tournamentName) {

		List<TournamentLeaderboard> finalLeaderboard = new ArrayList<>();

		Tournament tournament = viewTournamentByName(tournamentName);

		if (tournament == null)
			return null;

		List<TournamentLeaderboard> tournamentLeaderboard = leaderboardRepository
				.getInformationForTournamentLeaderboard(tournament.getId());

		if (tournamentLeaderboard.isEmpty())
			return null;

		String username = tournamentLeaderboard.get(0).getUsername();

		List<DateStatus> problemDurations = new ArrayList<>();
		for (int i = 0; i < tournamentLeaderboard.size(); i++) {

			TournamentLeaderboard t = tournamentLeaderboard.get(i);
			System.out.println(t.getUsername() + " - " + t.getOpeningDate() + " -  " + t.getCompletionDate());

			problemDurations.add(new DateStatus(t.getOpeningDate().getTime(), true));

			DateStatus completion;
			if (t.getCompletionDate() == null) { // user hasn't completed the problem
				if (tournament.getEndingDate().getTime() <= Calendar.getInstance().getTimeInMillis()) {
					// in case the tournament has ended use the tournament's ending date as
					// completion date
					completion = new DateStatus(tournament.getEndingDate().getTime(), false);
				} else {
					completion = new DateStatus(Calendar.getInstance().getTimeInMillis(), false); // the tournament is
																									// still active, so,
																									// use the current
																									// date
				}
			} else {
				completion = new DateStatus(t.getCompletionDate().getTime(), false);
			}

			problemDurations.add(completion);

			if (i + 1 < tournamentLeaderboard.size()) {

				TournamentLeaderboard t1 = tournamentLeaderboard.get(i + 1);

				if (!username.equals(t1.getUsername())) {
					long ms = DurationCalculation.calculateDuration(problemDurations);
					t.setTotalMilliseconds(ms);
					finalLeaderboard.add(new TournamentLeaderboard(t.getUsername(), t.getScore(), null, null,
							t.getTotalMilliseconds()));
					problemDurations = new ArrayList<>();

					username = t1.getUsername();
				}
			}

			if (i == tournamentLeaderboard.size() - 1) {
				long ms = DurationCalculation.calculateDuration(problemDurations);
				t.setTotalMilliseconds(ms);
				finalLeaderboard.add(
						new TournamentLeaderboard(t.getUsername(), t.getScore(), null, null, t.getTotalMilliseconds()));
			}

		}

		return finalLeaderboard;
	}

	@PostMapping(path = "/Tournament/add")
	public ResponseEntity<?> addTournament(@RequestBody Tournament t) {

		System.out.println(t.toString());

		Users u = viewUsersByUsername(t.getOwner().getUsername());
		Tournament current = viewTournamentByName(t.getName());

		if (current != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	
		if(t.getCode() != null && !t.getCode().equals("")) {
			current = tournamentRepository.findByCode(t.getCode());
			
			if(current == null) {
				return new ResponseEntity<>(HttpStatus.IM_USED);
			}
		}

		Tournament tournament = null;

		if (u != null) {
			tournament = new Tournament(t.getName(), t.getDescription(), t.getStartingDate(), t.getEndingDate(),
					t.getCode(), u, t.getShowWebsite());
		}

		tournament = tournamentRepository.save(tournament);
		return new ResponseEntity<>(tournament, HttpStatus.OK);
	}

	@PostMapping("/Tournament/update")
	public Tournament updateTournament(@RequestBody Tournament t) {
		Tournament tournamentUpdate = viewTournamentById(t.getId());
		if (tournamentUpdate == null)
			return null;

		tournamentUpdate.setCode(t.getCode());
		tournamentUpdate.setDescription(t.getDescription());
		tournamentUpdate.setEndingDate(t.getEndingDate());
		tournamentUpdate.setLink(t.getLink());
		tournamentUpdate.setName(t.getName());
		tournamentUpdate.setOpen(t.getOpen());
		tournamentUpdate.setOwner(t.getOwner());
		tournamentUpdate.setShowWebsite(t.getShowWebsite());
		tournamentUpdate.setStartingDate(t.getStartingDate());

		return tournamentRepository.save(tournamentUpdate);
	}

	@GetMapping("/Tournament/viewAllByOwnerId/{ownerId}")
	public List<Tournament> viewAllByOwnerId(@PathVariable long ownerId) {
		Optional<Users> u = viewUsersById(ownerId);
		List<Tournament> finalTournaments = new ArrayList<>();

		if (u.isPresent()) {
			finalTournaments = tournamentRepository.findByOwner(u.get());
		}
		return finalTournaments;
	}

	@GetMapping("/Tournament/viewAllWithRegisteredUsersByOwnerUsername/{username}")
	public List<TournamentWithRegisteredUsers> viewAllWithRegisteredUsersByOwnerUsername(
			@PathVariable String username) {
		Users u = viewUsersByUsername(username);

		List<Tournament> tournaments = new ArrayList<>();
		List<TournamentWithRegisteredUsers> tournamentWithRegisteredUsers = new ArrayList<>();

		if (u != null) {
			tournaments = tournamentRepository.findByOwner(u);

			for (Tournament t : tournaments) {
				List<Users> usersByTournament = viewUsersByTournamentId(t.getId());
				tournamentWithRegisteredUsers.add(new TournamentWithRegisteredUsers(t, usersByTournament));
			}

		}
		return tournamentWithRegisteredUsers;
	}

	// Returns 2 separate lists of available and archived problems
	@GetMapping(path = "/Tournament/viewTournamentsToList/{username}")
	public TournamentsToList getAllTournamentsToList(@PathVariable String username) {

		Users user = viewUsersByUsername(username);

		if (user == null) {
			return null;
		}

		TournamentsToList tournamentsToList = new TournamentsToList();

		List<TournamentIsUserRegistrated> availableTournaments = new ArrayList<>();
		List<TournamentIsUserRegistrated> archivedTournaments = new ArrayList<>();

		List<Tournament> allTournaments = getAllTournaments();

		boolean registered;
		for (Tournament t : allTournaments) {

			registered = isUserRegisteredInTournament(user.getId(), t.getId());

			if (!t.getShowWebsite() && !registered)
				continue;

			TournamentIsUserRegistrated tt = new TournamentIsUserRegistrated(t, registered);

			if (t.getEndingDate().getTime() >= System.currentTimeMillis()) {
				availableTournaments.add(tt);
			} else {
				archivedTournaments.add(tt);
			}
		}

		tournamentsToList.setAvailableTournaments(availableTournaments);
		tournamentsToList.setArchivedTournaments(archivedTournaments);

		return tournamentsToList;
	}

	@GetMapping(path = "/Tournament/getAllProblemsByName/{name}")
	public List<Problem> getAllProblemsByTournamentName(@PathVariable String name) {

		Tournament tournament = viewTournamentByName(name);

		if (tournament == null)
			return new ArrayList<>();

		return problemRepository.findAllByTournament(tournament);
	}

	// not sure if this should be here
	@PostMapping(path = "/Tournament/registerUser")
	public TournamentsToList registerUserOnTournament(@RequestBody RegisterUserOnTournament register) {

		Users userByUsername = viewUsersByUsername(register.getUser().getUsername());

		System.out.println("TOURNAMENT ID" + register.getTournament().getId());
		Tournament tournament = null;
		if (register.getTournament().getId() != 0) {
			tournament = viewTournamentById(register.getTournament().getId());
		} else {
			tournament = tournamentRepository.findByCode(register.getTournament().getCode());
		}
		System.out.println(register.toString());
		if (userByUsername == null || tournament == null
				|| (tournament.getCode() != null && register.getTournament().getCode() != null
						&& (!tournament.getCode().equals(register.getTournament().getCode())))) {
			return getAllTournamentsToList(userByUsername.getUsername()); // ?!
		}

		Rating r = new Rating(tournament, userByUsername, (double) -1);
		ratingRepository.save(r);

		return getAllTournamentsToList(userByUsername.getUsername());

	}

	@PostMapping(path = "/Tournament/calculateRatings/{tournamentId}")
	public void calculateTournamentRatings(@PathVariable long tournamentId) {

		List<Users> usersUpdated = new ArrayList<>();
		Tournament tournament = viewTournamentById(tournamentId);

		if (tournament == null) {
			return;
		}

		List<Users> usersPerTournament = viewUsersByTournamentId(tournamentId);

		for (int i = 0; i < usersPerTournament.size(); i++) {

			Users currentUser = usersPerTournament.get(i);

			double sumExpectedRating = 0;
			double sumPoints = 0;

			for (int j = 0; j < usersPerTournament.size(); j++) {

				Users opponent = usersPerTournament.get(j);
				if (currentUser == opponent)
					continue;

				sumExpectedRating += RatingCalculator.expectedRating(currentUser.getGlobalRating(),
						opponent.getGlobalRating());

				double scoreA = tournamentRepository.findScoreOfUserInTournament(currentUser.getId(),
						tournament.getId());
				double scoreB = tournamentRepository.findScoreOfUserInTournament(opponent.getId(), tournament.getId());

				sumPoints += RatingCalculator.pointsComparasion(scoreA, scoreB);

			}

			double calculatedRating = (currentUser.getGlobalRating()
					+ RatingCalculator.K * (sumPoints - sumExpectedRating));

			// Updates rating for current tournament
			Optional<Rating> currentRating = ratingRepository
					.findById(new RatingID(tournament.getId(), currentUser.getId()));
			if (currentRating.isPresent()) {
				currentRating.get().setElo(calculatedRating);
			}

			// Updates rating on overall leaderboard
			Users user = currentUser;
			user.setGlobalRating(calculatedRating);
			usersUpdated.add(user);
		}

		// saves all ratings after calculating.
		// Can't be done on the for loop because updating a rating before calculating
		// them all would result in incorrect data
		usersRepository.saveAll(usersUpdated);

	}

	@PostMapping(path = "/Tournament/delete/{id}")
	public void deleteTournamentById(@PathVariable long id) {
		Tournament tournament = viewTournamentById(id);

		if (tournament != null) {
			List<Problem> problemsByTournament = problemRepository.findAllByTournament(tournament);

			for (Problem p : problemsByTournament) {
				p.setTournament(null);
			}

			problemRepository.saveAll(problemsByTournament);
		}
		tournamentRepository.deleteById(id);
	}

	@DeleteMapping(path = "/Tournament/deleteByName/{tournamentName}")
	public void deleteTournament(@PathVariable String tournamentName) {
		tournamentName = tournamentName.replaceAll("-", " ");
		Tournament t = tournamentRepository.findByName(tournamentName);

		if (t != null) {
			List<Problem> problemsByTournament = problemRepository.findAllByTournament(t);

			for (Problem p : problemsByTournament) {
				p.setTournament(null);
			}

			problemRepository.saveAll(problemsByTournament);
			tournamentRepository.delete(t);
		}
	}

	@GetMapping(path = "/Tournament/view/{id}")
	public Tournament viewTournamentById(@PathVariable long id) {
		Optional<Tournament> t = tournamentRepository.findById(id);
		if (t.isPresent()) {
			return tournamentRepository.save(t.get());
		}
		return null;
	}

	// USERS

	@GetMapping(path = "/Users/view")
	public List<Users> getAllUsers() {
		return (List<Users>) usersRepository.findAll();
	}

	@GetMapping(path = "/Users/viewByUsername/{username}")
	public Users viewUsersByUsername(@PathVariable String username) {
		return usersRepository.findByUsername(username);
	}

	@GetMapping("/Users/viewAllWithLessInfo")
	public List<UserLessInfo> viewAllWithLessInfo() {
		List<UserLessInfo> finalUsers = new ArrayList<>();
		List<Users> allUsers = getAllUsers();

		for (Users u : allUsers) {
			finalUsers.add(new UserLessInfo(u.getId(), u.getUsername(), u.getGlobalRating()));
		}

		return finalUsers;
	}

	@PostMapping(path = "/Users/add")
	public void addUsers(@RequestBody Users user) {
		usersController.register(user);
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

}
