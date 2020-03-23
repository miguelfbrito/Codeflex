package pt.codeflex.dto;

import javax.persistence.Column;

public class TestCasesShown {

	private long id;

	// TODO : change to files instead of String's because it will probably overflow
	// for large data
	// TODO : change 1 -> N to N -> N ?

	@Column(length = 5000)
	private String input;

	@Column(length = 5000)
	private String output;

	private String description;

	private boolean shown;

	public TestCasesShown() {
	}

	public TestCasesShown(String input, String output) {
		this.input = input;
		this.output = output;
		this.shown = false;
	}

	public TestCasesShown(String input, String output, String description, boolean shown) {
		this.input = input;
		this.output = output;
		this.description = description;
		this.shown = shown;
	}

	public TestCasesShown(long id, String input, String output, String description, boolean shown) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.description = description;
		this.shown = shown;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
