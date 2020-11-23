package edu.cs.fsu.tradish;

import java.util.Objects;

public class User {
    String mUsername;

    public User() {}

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(mUsername, user.mUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUsername);
    }
}
