package pt.codeflex.databasecompositekeys;

import java.io.Serializable;

import javax.persistence.Embeddable;

// TODO : implement hashCode and equals methods
@Embeddable
public class DurationsID implements Serializable {

    private long users;
    private long problems;

    public DurationsID() {
    }

    public DurationsID(long users, long problems) {
        this.users = users;
        this.problems = problems;
    }

    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public long getProblems() {
        return problems;
    }

    public void setProblems(long problems) {
        this.problems = problems;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (problems ^ (problems >>> 32));
        result = prime * result + (int) (users ^ (users >>> 32));
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
        DurationsID other = (DurationsID) obj;
        if (problems != other.problems)
            return false;
        if (users != other.users)
            return false;
        return true;
    }

}
