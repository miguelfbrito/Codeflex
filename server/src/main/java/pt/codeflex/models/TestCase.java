package pt.codeflex.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TestCase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// TODO : change to files instead of String's because it will probably overflow
	// for large data
	// TODO : change 1 -> N to N -> N ?

	@Column(length = 7500)
	private String input;

	@Column(length = 7500)
	private String output;

	@Column(length = 2500)
	private String description;

	private boolean shown;

	public TestCase() {
	}

	public TestCase(String input, String output) {
		this.input = input;
		this.output = output;
		this.shown = false;
	}

	public TestCase(String input, String output, String description, boolean shown) {
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