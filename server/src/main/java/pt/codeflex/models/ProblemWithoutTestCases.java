package pt.codeflex.models;

import pt.codeflex.databasemodels.Difficulty;

public class ProblemWithoutTestCases {

	private long id;
	private String name;
	private String description;
	private Difficulty difficulty;
	private boolean solved;

	public ProblemWithoutTestCases(long id, String name, String description, Difficulty difficulty) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
	}

	public ProblemWithoutTestCases(long id, String name, String description, Difficulty difficulty, boolean solved) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.solved = solved;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}
}
