package pl.polsl.michal.sadkowski.java1.sudoku.model;

/**
 * Simple user object.
 * Some methods are not here.
 * Only store name now.
 *
 * @author Michał Sadkowski
 * @version 1.1
 */
public class User {
    /** The name of player. */
    private String username;

    /**
     * Make a User object. Set name to "guest" if name is null or empty.
     *
     * @param username the name user want.
     */
    public User(String username) {
        if (username == null || username.isEmpty()) this.username = "guest";
        else this.username = username;
    }

    /**
     * Give the current name.
     *
     * @return the name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set new name if string is not null or empty.
     *
     * @param username the new name.
     */
    public void setUsername(String username) {
        if (username == null || username.isEmpty()) return;
        this.username = username;
    }

    /**
     * This is method for saving state. Not implemented.
     */
    public void saveState() {
        // To implement later
    }

    /**
     * Give a string text for the User object.
     *
     * @return a string with the name.
     */
    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + '}';
    }
}