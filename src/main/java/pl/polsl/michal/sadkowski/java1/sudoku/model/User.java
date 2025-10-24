package pl.polsl.michal.sadkowski.java1.sudoku.model;

/**
 * Simple user object representing the player.
 * Currently, only the player's username is stored.
 *
 * @author Michał Sadkowski
 * @version 1.1
 */
public class User {
    /** The name of the player. */
    private String username;

    /**
     * Creates a User object. Sets the name to "guest" if the provided name is null or empty.
     *
     * @param username The desired name for the user.
     */
    public User(String username) {
        if (username == null || username.isEmpty()) this.username = "guest";
        else this.username = username;
    }

    /**
     * Gets the current username.
     *
     * @return The current username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets a new username if the provided string is not null or empty.
     *
     * @param username The new username.
     */
    public void setUsername(String username) {
        if (username == null || username.isEmpty()) return;
        this.username = username;
    }

    /**
     * Placeholder method for saving the user's state. Not implemented yet.
     */
    public void saveState() {
        // To implement later
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return A string containing the username.
     */
    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + '}';
    }
}