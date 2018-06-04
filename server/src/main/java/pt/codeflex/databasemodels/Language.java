package pt.codeflex.databasemodels;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.transaction.Transactional;

@Entity
public class Language {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	private String compilerName;

	public Language() {
	}

	public Language(String name, String compilerName) {
		super();
		this.name = name;
		this.compilerName = compilerName;
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

	public String getCompilerName() {
		return compilerName;
	}

	public void setCompilerName(String compilerName) {
		this.compilerName = compilerName;
	}

	@Override
	public String toString() {
		return "Language [id=" + id + ", name=" + name + ", compilerName=" + compilerName + "]";
	}

}
