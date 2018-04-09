package pt.codeflex.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class TestCases {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	// TODO : change to files instead of String's because it will probably overflow
	// for large data
	// TODO : change 1 -> N to N -> N ?

	@Column(length = 5000)
	private String input;

	public TestCases() {
	}

	@Column(length = 5000)
	private String output;

	@OneToMany
	@JoinColumn(name = "testcases_id")
	private List<Scoring> scoring = new ArrayList<>();

	public TestCases(String input, String output) {
		this.input = input;
		this.output = output;
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

	public List<Scoring> getScoring() {
		return scoring;
	}

	public void setScoring(List<Scoring> scoring) {
		this.scoring = scoring;
	}
}