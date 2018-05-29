package pt.codeflex.databasecomposites;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class GroupsProblemID implements Serializable {

	private long groups;
	private long problem;

	public GroupsProblemID(long groups, long problem) {
		super();
		this.groups = groups;
		this.problem = problem;
	}

	public long getGroups() {
		return groups;
	}

	public void setGroups(long groups) {
		this.groups = groups;
	}

	public long getProblem() {
		return problem;
	}

	public void setProblem(long problem) {
		this.problem = problem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (groups ^ (groups >>> 32));
		result = prime * result + (int) (problem ^ (problem >>> 32));
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
		GroupsProblemID other = (GroupsProblemID) obj;
		if (groups != other.groups)
			return false;
		if (problem != other.problem)
			return false;
		return true;
	}

}
