package pt.codeflex.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import pt.codeflex.databasecompositekeys.DurationsID;

@IdClass(DurationsID.class)
@Entity
public class Durations implements Serializable {

	@Id
	@ManyToOne
	private Users users;

	@Id
	@ManyToOne
	private Problem problems;

	private Date openingDate;
	private Date completionDate;

	public Durations() {
	}

	public Durations(Users users, Problem problems, Date openingDate, Date completionDate) {
		super();
		this.users = users;
		this.problems = problems;
		this.openingDate = openingDate;
		this.completionDate = completionDate;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Problem getProblems() {
		return problems;
	}

	public void setProblems(Problem problems) {
		this.problems = problems;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((problems == null) ? 0 : problems.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Durations other = (Durations) obj;
		if (problems == null) {
			if (other.problems != null)
				return false;
		} else if (!problems.equals(other.problems))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

}
