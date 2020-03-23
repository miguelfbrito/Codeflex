package pt.codeflex.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import pt.codeflex.databasecompositekeys.GroupsProblemID;

@IdClass(GroupsProblemID.class)
@Entity
public class GroupsProblem {

	@Id
	@ManyToOne
	private Groups groups;

	@Id
	@ManyToOne
	private Problem problem;

	public Groups getGroups() {
		return groups;
	}

	public GroupsProblem(Groups groups, Problem problem) {
		super();
		this.groups = groups;
		this.problem = problem;
	}

	public void setGroups(Groups groups) {
		this.groups = groups;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + ((problem == null) ? 0 : problem.hashCode());
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
		GroupsProblem other = (GroupsProblem) obj;
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		if (problem == null) {
			if (other.problem != null)
				return false;
		} else if (!problem.equals(other.problem))
			return false;
		return true;
	}

}
