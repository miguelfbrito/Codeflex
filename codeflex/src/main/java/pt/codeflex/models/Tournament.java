package pt.codeflex.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Tournament {

	@Id
	@SequenceGenerator(name = "seq_tournament_id", sequenceName = "seq_tournament_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_tournament_id")
	private long id;

	@Column(length = 50)
	private String name;

	@Column(length = 10000)
	private String description;

	public Tournament() {}
	
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

}
