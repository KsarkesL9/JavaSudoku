package pl.polsl.michal.sadkowski.java1.sudoku.model;

/**
 * Class representing the entire Sudoku game session.
 * It holds the board and user information, acting as the main entry point to the model layer.
 *
 * @author Michał Sadkowski
 * @version 1.1
 */
public class SudokuGame {
    /** The main Sudoku board. */
    private final Board board;
    /** The player information. */
    private final User user;

    /**
     * Creates a new Sudoku game session, initializing the board and user.
     *
     * @param username The name of the player.
     */
    public SudokuGame(String username) {
        this.board = new Board();
        this.user = new User(username);
    }

    /**
     * Gets the board object to perform cell operations.
     *
     * @return The Sudoku board instance.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the user object.
     *
     * @return The current user instance.
     */
    public User getUser() {
        return user;
    }

    /**
     * Checks if the board is completely filled and correctly solved.
     * Note: The solving logic is currently not implemented.
     *
     * @return {@code true} if the game is won, {@code false} otherwise.
     */
    public boolean checkWin() {
        // Must implement later
        return false;
    }
}