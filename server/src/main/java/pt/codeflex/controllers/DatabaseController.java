package pt.codeflex.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.codeflex.databasecompositekeys.DurationsID;
import pt.codeflex.databasecompositekeys.RatingID;
import pt.codeflex.models.*;
import pt.codeflex.dto.CategoriesWithoutTestCases;
import pt.codeflex.dto.DateStatus;
import pt.codeflex.dto.ListCategoriesWithStats;
import pt.codeflex.dto.ProblemDetails;
import pt.codeflex.dto.AddTournamentToProblem;
import pt.codeflex.dto.ProblemWithoutTestCases;
import pt.codeflex.dto.RegisterUserOnTournament;
import pt.codeflex.dto.TestCasesShown;
import pt.codeflex.dto.TournamentIsUserRegistrated;
import pt.codeflex.dto.TournamentLeaderboard;
import pt.codeflex.dto.TournamentWithRegisteredUsers;
import pt.codeflex.dto.TournamentsToList;
import pt.codeflex.dto.UserLessInfo;
import pt.codeflex.dto.UsersLeaderboard;
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

	// MOVED
	@GetMapping("/Durations/view")
	public List<Durations> getAllDurations() {
		return durationsRepository.findAll();
	}

	// MOVED
	@GetMapping("/Durations/viewById/{userId}/{problemId}")
	public Durations viewDurationsById(@PathVariable long userId, @PathVariable long problemId) {
		Optional<Durations> d = durationsRepository.findById(new DurationsID(userId, problemId));
		if (d.isPresent()) {
			return d.get();
		}
		return new Durations();
	}



	// MOVED
	@PostMapping("/Durations/onProblemOpening")
	public Durations addDurationOnProblemOpening(@RequestBody Durations duration) {

		Users users = viewUsersByUsername(duration.getUsers().getUsername());
		Problem problem = viewProblemById(duration.getProblems().getId());

		Durations newDuration = new Durations();

		if (users != null && problem != null) {

			newDuration.setUsers(users);
			newDuration.setProblems(problem);

			boolean openedAlready = durationsRepository.existsById(new DurationsID(users.getId(), problem.getId()));

			if (openedAlready) {
				return new Durations();
			}

			newDuration.setOpeningDate(Calendar.getInstance().getTime());

			return durationsRepository.save(newDuration);
		}

		return newDuration;

	}

	// MOVED
	@PostMapping("/Durations/onProblemCompletion")
	public Durations updateDurationsOnProblemCompletion(@RequestBody Durations duration) {

		Users users = viewUsersByUsername(duration.getUsers().getUsername());
		Problem problem = viewProblemById(duration.getProblems().getId());

		if (users != null && problem != null) {

			Optional<Durations> d = durationsRepository.findById(new DurationsID(users.getId(), problem.getId()));

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

	// MOVED
	@GetMapping(path = "/Leaderboard/view")
	public List<Leaderboard> getAllLeaderboards() {
		return leaderboardRepository.findAll();
	}


	@GetMapping(path = "/Leaderboard/viewByProblemName/{problemName}")
	public List<UsersLeaderboard> getAllLeaderboardsByProblemName(@PathVariable String problemName) {

		Problem findProblembyName = viewProblemByName(problemName);
		List<UsersLeaderboard> userOnLeaderboard = new ArrayList<>();

		if (findProblembyName != null) {
			List<Leaderboard> findByProblem = leaderboardRepository.findAllByProblem(findProblembyName);
			for (Leaderboard l : findByProblem) {

				Durations currentUserDuration = viewDurationsById(l.getUsers().getId(), l.getProblem().getId());
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
				userOnLeaderboard.add(new UsersLeaderboard(l.getUsers().getUsername(), l.getScore(), l.getLanguage(),
						durationMilliseconds));
			}

		}

		return userOnLeaderboard;
	}

	@PostMapping(path = "/Leaderboard/add")
	public Leaderboard addLeaderboard(@RequestBody Leaderboard leaderboard) {
		return leaderboardRepository.save(new Leaderboard(leaderboard.getScore(), leaderboard.getUsers(),
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

	// MOVED
	@GetMapping(path = "/Problem/view")
	public List<Problem> getAllProblems() {
		return problemRepository.findAll();
	}

	// MOVED
	@GetMapping(path = "/Problem/viewByName/{name}")
	public Problem viewProblemByName(@PathVariable String name) {
		return problemRepository.findByName(name.replace("-", " "));
	}

	// MOVED
	@GetMapping(path = "/Problem/viewAllByOwnerUsername/{username}")
	public List<Problem> viewAllByOwnerUsername(@PathVariable String username) {
		Users owner = viewUsersByUsername(username);
		if (owner != null) {
			return problemRepository.findAllByOwner(owner);

		}
		return new ArrayList<>();
	}

	// MOVED
	@GetMapping(path = "/Problem/viewAllWithCategory")
	public List<Problem> viewAllProblemsWithCategory() {
		List<Problem> finalProblems = new ArrayList<>();
		List<Problem> problems = getAllProblems();
		List<PractiseCategory> categories = viewAllPractiseCategories();

		for (PractiseCategory pc : categories) {
			for (Problem p : problems) {
				if (pc.getProblem().contains(p)) {
					finalProblems.add(p);
				}
			}
		}
		return finalProblems;
	}

	// MOVED
	@GetMapping(path = "/Problem/viewAllDetails/{problemName}")
	public ProblemDetails viewAllProblemDetails(@PathVariable String problemName) {
		Problem problem = viewProblemByName(problemName);

		ProblemDetails p = new ProblemDetails();
		if (problem != null) {
			p.setProblem(problem);
		}

		PractiseCategory pc = viewPractiseCategoryByProblemName(problem.getName());
		if (pc != null) {
			p.setCategory(pc);
		}

		return p;
	}

	// MOVED
	@PostMapping(path = "/Problem/add")
	public Problem addProblem(@RequestBody ProblemDetails problemDetails) {

		Problem p = problemDetails.getProblem();

		if (p == null) {
			return new Problem();
		}

		if (problemDetails.getDifficulty() != null) {
			Optional<Difficulty> f = difficultyRepository.findById(problemDetails.getDifficulty().getId());
			f.ifPresent(p::setDifficulty);
		}

		// A problem can only belong to either a Tournament or a Category
		if (problemDetails.getTournament() != null && problemDetails.getCategory() != null) {
			System.out.println(problemDetails.getTournament());
			System.out.println(problemDetails.getCategory());
			System.out.println(problemDetails.getProblem().getDifficulty());
			if (problemDetails.getTournament().getName() != null && problemDetails.getCategory().getName() != null
					&& !problemDetails.getCategory().getName().equals("")) {
				return new Problem();
			}
		}

		if (problemDetails.getTournament() != null) {
			if (problemDetails.getTournament().getName() != null) {
				Tournament t = viewTournamentByName(problemDetails.getTournament().getName());
				if (t != null) {
					p.setTournament(t);
				}
			}
		}

		if (problemDetails.getOwner() != null) {
			Optional<Users> u = usersRepository.findById(problemDetails.getOwner().getId());
			if (u.isPresent()) {
				System.out.println(u.get().toString());
				p.setOwner(u.get());
			} else {
				Users users = usersRepository.findByUsername(problemDetails.getOwner().getUsername());
				if (u.isPresent()) {
					p.setOwner(users);
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

		if (problemDetails.getCategory() != null) {
			PractiseCategory category = viewPractiseCategoryById(problemDetails.getCategory().getId());
			if (category != null) {
				category.getProblem().add(p);
				practiseCategoryRepository.save(category);
			}
		}

		return savedProblem;
	}

	// MOVED
	@PostMapping("/Problem/update")
	public Problem updateProblem(@RequestBody ProblemDetails changes) {

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

	// MOVED
	@PostMapping(path = "/Problem/addTestCase")
	public void addTestCasesToProblem(@RequestParam long problemId, @RequestParam long testCaseId) {
		Optional<TestCase> tc = testCasesRepository.findById(testCaseId);
		Optional<Problem> p = problemRepository.findById(problemId);

		if (p.isPresent() && tc.isPresent()) {
			Problem problem = p.get();
			problem.getTestCases().add(tc.get());
			problemRepository.save(problem);
		}
	}

	// MOVED
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


	// MOVED
	@GetMapping("/Problem/viewAllTestCasesByProblemName/{problemName}")
	public List<TestCasesShown> viewAllTestCasesByProblemName(@PathVariable String problemName) {
		List<TestCasesShown> finalTestCases = new ArrayList<>();
		Problem p = viewProblemByName(problemName);

		if (p != null) {
			List<TestCase> testCases = p.getTestCases();
			for (TestCase tc : testCases) {
				finalTestCases.add(new TestCasesShown(tc.getId(), tc.getInput(), tc.getOutput(), tc.getDescription(),
						tc.isShown()));
			}
		}
		return finalTestCases;
	}

	// MOVED
	@PostMapping(path = "/Problem/delete/{id}")
	public void deleteProblem(@PathVariable long id) {
		problemRepository.deleteById(id);
	}

	//MOVED
	@PostMapping(path = "/Problem/deleteByName/{name}")
	public String deleteProblemByName(@PathVariable String name) {
		Problem p = viewProblemByName(name);
		if (p != null) {
			problemRepository.deleteById(p.getId());
		}
		return "";
	}

	// MOVED
	@GetMapping(path = "/Problem/view/{id}")
	public Problem viewProblemById(@PathVariable long id) {
		Optional<Problem> problem = problemRepository.findById(id);

		if (problem.isPresent()) {
			return problem.get();
		}
		return null;
	}

	// MOVED
	@GetMapping(path = "/Problem/getProblemByName/{problemName}")
	public Problem getProblemByName(@PathVariable String problemName) {
		problemName = problemName.replace("-", " ");
		Problem p = problemRepository.findByName(problemName);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (p != null) {

			if (p.getTournament() != null
					&& !isUserRegisteredInTournament(1,1)) {
				return new Problem();
			}

			return p;
		}
		return new Problem();
	}

	// RATING

	// MOVED
	@GetMapping(path = "/Rating/view")
	public List<Rating> getAllRatings() {
		return ratingRepository.findAll();
	}

	// MOVED
	@GetMapping(path = "/Rating/viewUsersByTournamentId/{tournamentId}")
	public List<Users> viewUsersByTournamentId(@PathVariable long tournamentId) {

		List<Users> usersInTournament = new ArrayList<>();
		List<Rating> allRatings = getAllRatings();

		Tournament t = viewTournamentById(tournamentId);

		if (t == null) {
			return usersInTournament;
		}

		for (Rating r : allRatings) {
			if (r.getTournament().getId() == t.getId()) {
				usersInTournament.add(r.getUsers());
			}
		}

		return usersInTournament;
	}


	// MOVED
	@GetMapping(path = "/Rating/isUserRegisteredInTournament/{userId}/{tournamentId}")
	public boolean isUserRegisteredInTournament(@PathVariable long userId, @PathVariable long tournamentId) {
		Optional<Rating> r = ratingRepository.findById(new RatingID(tournamentId, userId));
		Tournament tournament = viewTournamentById(tournamentId);

		if (tournament == null) {
			return false;
		}

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
		Optional<TestCase> tc = testCasesRepository.findById(testCasesId);

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
		Users users = usersRepository.findByUsername(username.trim());
		List<Submissions> submissions = submissionsRepository.findAll();
		List<Submissions> finalSubmissions = new ArrayList<>();
		if (users != null && submissions != null) {
			for (Submissions s : submissions) {
				if (s.getUsers() == users) {
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
			Users users = u.get();
			Problem problem;
			if (p.isPresent()) {
				problem = p.get();
				Language lang = languageRepository.findByName(language);

				Submissions s = new Submissions(problem, lang, code, users);
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
	public Iterable<TestCase> getAllTestCases() {
		return testCasesRepository.findAll();
	}

	@PostMapping(path = "/TestCases/add-deprecated")
	public void addTestCasesDeprecated(@RequestParam String input, @RequestParam String output) {
		TestCase tc = new TestCase(input, output);
		testCasesRepository.save(tc);
	}

	@PostMapping(path = "/TestCases/add")
	public TestCase addTestCases(@RequestBody TestCasesShown t) {
		TestCase tc = new TestCase(t.getInput(), t.getOutput(), t.getDescription(), t.isShown());
		return testCasesRepository.save(tc);
	}

	@PostMapping("/TestCases/addToProblem/{problemName}")
	public void addTestCasesToProblem(@RequestBody TestCasesShown t, @PathVariable String problemName) {

		TestCase testCase = addTestCases(t);

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
		Optional<TestCase> t = testCasesRepository.findById(id);

		if (t.isPresent()) {
			TestCase testCase = t.get();
			testCasesRepository.save(testCase);
		}
	}

	@PostMapping(path = "/TestCases/delete/{id}")
	public void deleteTestCases(@PathVariable long id) {
		testCasesRepository.deleteById(id);
	}

	@GetMapping(path = "/TestCases/view/{id}")
	public Optional<TestCase> viewTestCasesById(@PathVariable long id) {
		return testCasesRepository.findById(id);
	}

	@PostMapping("/TestCases/update")
	public TestCase updateTestCase(@RequestBody TestCasesShown testCaseReq) {
		Optional<TestCase> tc = viewTestCasesById(testCaseReq.getId());
		if (tc.isPresent()) {
			TestCase testCase = tc.get();

			testCase.setInput(testCaseReq.getInput());
			testCase.setOutput(testCaseReq.getOutput());
			testCase.setDescription(testCaseReq.getDescription());
			testCase.setShown(testCaseReq.isShown());

			return testCasesRepository.save(testCase);
		}
		return new TestCase();
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

	// @GetMapping("/Tournament/viewCurrentLeaderboard/{tournamentName}")
	// public UsersLeaderboard viewCurrentLeaderboard(@PathVariable String
	// tournamentName) {
	//
	// Tournament tournament = viewTournamentByName(tournamentName);
	//
	// if (tournament == null)
	// return null;
	//
	// List<Users> usersInTournament = viewUsersByTournamentId(tournament.getId());
	//
	// List<Problem> problemsInTournament =
	// getAllProblemsByTournamentName(tournament.getName());
	//
	// for (Users u : usersInTournament) {
	//
	// }
	//
	// return null;
	// }

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
		double totalScore = 0;
		for (int i = 0; i < tournamentLeaderboard.size(); i++) {

			TournamentLeaderboard t = tournamentLeaderboard.get(i);
			totalScore += t.getScore();

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
					finalLeaderboard.add(new TournamentLeaderboard(t.getUsername(), totalScore, null, null,
							t.getTotalMilliseconds()));
					problemDurations = new ArrayList<>();
					totalScore = 0;
					username = t1.getUsername();
				}
			}

			if (i == tournamentLeaderboard.size() - 1) { // - 1
				long ms = DurationCalculation.calculateDuration(problemDurations);
				t.setTotalMilliseconds(ms);
				finalLeaderboard.add(
						new TournamentLeaderboard(t.getUsername(), totalScore, null, null, t.getTotalMilliseconds()));
			}

		}

		return finalLeaderboard;
	}

	@GetMapping("/Tournament/isUserTournamentOwner/{tournamentName}/{username}")
	public ResponseEntity<?> isUserTournamentOwner(@PathVariable String tournamentName, @PathVariable String username) {

		Users users = viewUsersByUsername(username);

		if (users == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		Tournament tournament = viewTournamentByName(tournamentName);

		if (tournament == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		if (tournament.getOwner().getId() == users.getId()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PostMapping(path = "/Tournament/add")
	public ResponseEntity<?> addTournament(@RequestBody Tournament t) {

		System.out.println(t.toString());

		Users u = viewUsersByUsername(t.getOwner().getUsername());
		Tournament current = viewTournamentByName(t.getName());

		if (current != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		if (t.getCode() != null && !t.getCode().equals("")) {

			current = tournamentRepository.findByCode(t.getCode());

			if (current != null && t.getCode().equals(current.getCode())) {
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

		Users users = viewUsersByUsername(t.getOwner().getUsername());

		if (tournamentUpdate == null && users != null) // ?!
			return null;

		tournamentUpdate.setCode(t.getCode());
		tournamentUpdate.setDescription(t.getDescription());
		tournamentUpdate.setEndingDate(t.getEndingDate());
		tournamentUpdate.setLink(t.getLink());
		tournamentUpdate.setName(t.getName());
		tournamentUpdate.setOpen(t.getOpen());
		tournamentUpdate.setOwner(users);
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

	@GetMapping("/Tournament/viewAllPublicTournaments")
	public List<TournamentWithRegisteredUsers> viewAllPublicTournaments() {
		List<TournamentWithRegisteredUsers> tournamentWithRegisteredUsers = new ArrayList<>();
		List<Tournament> tournaments = tournamentRepository.findAll();

		for (Tournament t : tournaments) {
			if (t.getShowWebsite()) {

				List<Users> usersByTournament = viewUsersByTournamentId(t.getId());
				tournamentWithRegisteredUsers.add(new TournamentWithRegisteredUsers(t, usersByTournament));

			}
		}

		return tournamentWithRegisteredUsers;

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
				if (!t.getShowWebsite()) {
					List<Users> usersByTournament = viewUsersByTournamentId(t.getId());
					tournamentWithRegisteredUsers.add(new TournamentWithRegisteredUsers(t, usersByTournament));
				}
			}

		}
		return tournamentWithRegisteredUsers;
	}

	// Returns 2 separate lists of available and archived problems
	@GetMapping(path = "/Tournament/viewTournamentsToList/{username}")
	public TournamentsToList getAllTournamentsToList(@PathVariable String username) {

		Users users = viewUsersByUsername(username);

		if (users == null) {
			return null;
		}

		TournamentsToList tournamentsToList = new TournamentsToList();

		List<TournamentIsUserRegistrated> availableTournaments = new ArrayList<>();
		List<TournamentIsUserRegistrated> archivedTournaments = new ArrayList<>();

		List<Tournament> allTournaments = getAllTournaments();

		boolean registered;
		for (Tournament t : allTournaments) {

			registered = isUserRegisteredInTournament(users.getId(), t.getId());

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
		System.out.println(tournament.toString());
		if (tournament == null)
			return new ArrayList<>();

		return problemRepository.findAllByTournament(tournament);
	}

	// not sure if this should be here
	@PostMapping(path = "/Tournament/registerUser")
	public TournamentsToList registerUserOnTournament(@RequestBody RegisterUserOnTournament register) {

		Users usersByUsername = viewUsersByUsername(register.getUsers().getUsername());

		System.out.println("TOURNAMENT ID" + register.getTournament().getId());
		Tournament tournament = null;
		if (register.getTournament().getId() != 0) {
			tournament = viewTournamentById(register.getTournament().getId());
		} else {
			tournament = tournamentRepository.findByCode(register.getTournament().getCode());
		}
		System.out.println(register.toString());
		if (usersByUsername == null || tournament == null
				|| (tournament.getCode() != null && register.getTournament().getCode() != null
						&& (!tournament.getCode().equals(register.getTournament().getCode())))) {
			return getAllTournamentsToList(usersByUsername.getUsername()); // ?!
		}

		Rating r = new Rating(tournament, usersByUsername, (double) -1);
		ratingRepository.save(r);

		return getAllTournamentsToList(usersByUsername.getUsername());

	}

	@PostMapping(path = "/Tournament/calculateRatings/{tournamentId}")
	public void calculateTournamentRatings(@PathVariable long tournamentId) {

		List<Users> usersUpdated = new ArrayList<>();
		Tournament tournament = viewTournamentById(tournamentId);

		if (tournament == null) {
			return;
		}

		List<Users> usersPerTournament = viewUsersByTournamentId(tournament.getId());

		for (int i = 0; i < usersPerTournament.size(); i++) {

			Users currentUsers = usersPerTournament.get(i);

			double sumExpectedRating = 0;
			double sumPoints = 0;

			for (int j = 0; j < usersPerTournament.size(); j++) {

				Users opponent = usersPerTournament.get(j);
				if (currentUsers == opponent)
					continue;

				sumExpectedRating += RatingCalculator.expectedRating(currentUsers.getGlobalRating(),
						opponent.getGlobalRating());

				double scoreA = tournamentRepository.findScoreOfUserInTournament(currentUsers.getId(),
						tournament.getId());
				double scoreB = tournamentRepository.findScoreOfUserInTournament(opponent.getId(), tournament.getId());

				sumPoints += RatingCalculator.pointsComparasion(scoreA, scoreB);

			}

			double calculatedRating = (currentUsers.getGlobalRating()
					+ RatingCalculator.K * (sumPoints - sumExpectedRating));

			// Updates rating for current tournament
			Optional<Rating> currentRating = ratingRepository
					.findById(new RatingID(tournament.getId(), currentUsers.getId()));
			if (currentRating.isPresent()) {
				currentRating.get().setElo(calculatedRating);
			}

			// Updates rating on overall leaderboard
			Users users = currentUsers;
			users.setGlobalRating(calculatedRating);
			usersUpdated.add(users);
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

	@PostMapping(path = "/Tournament/deleteByName/{tournamentName}")
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
	public void addUsers(@RequestBody Users users) {
		usersController.register(users);
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
