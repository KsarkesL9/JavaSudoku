package pl.polsl.michal.sadkowski.java1.sudoku.model;

/**
 * Class representing the entire Sudoku game session.
 * It holds the board and user information.
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
     * Make new Sudoku game session.
     *
     * @param username The name of the player.
     */
    public SudokuGame(String username) {
        this.board = new Board();
        this.user = new User(username);
    }

    /**
     * Get the board object for work.
     *
     * @return The Sudoku board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Get the user object.
     *
     * @return The current user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Check if board is finished and correct.
     * This is not implemented yet.
     *
     * @return true if game is won, false if not.
     */
    public boolean checkWin() {
        // Must implement later
        return false;
    }
}