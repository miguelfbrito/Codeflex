package pt.codeflex.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class PractiseCategory {
	
	@Id
	@SequenceGenerator(name = "seq_practisecategory_id", sequenceName = "seq_practisecategory_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_practisecategory_id")
	private long id;
	
	private String name;

	public PractiseCategory() {}
	
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
}
