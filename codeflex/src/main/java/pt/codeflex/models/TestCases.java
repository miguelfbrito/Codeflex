package pt.codeflex.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class TestCases {

	@Id
	@SequenceGenerator(name = "seq_testcases_id", sequenceName = "seq_testcases_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_testcases_id")
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

}
