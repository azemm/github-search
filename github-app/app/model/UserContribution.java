package model;

import java.util.Comparator;

/**
 * Created by zem on 01/11/15.
 */
public class UserContribution{
    private User user;
    private long commits;

    public UserContribution(User user, long commits) {
        this.user = user;
        this.commits = commits;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getCommits() {
        return commits;
    }

    public void setCommits(long commits) {
        this.commits = commits;
    }
}
