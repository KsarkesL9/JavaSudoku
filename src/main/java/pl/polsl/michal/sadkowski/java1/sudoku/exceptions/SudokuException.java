package pl.polsl.michal.sadkowski.java1.sudoku.exceptions;

/**
 * Custom checked exception for Sudoku model operations.
 * This exception is thrown when a user attempts an invalid operation 
 * or breaks a Sudoku rule on the board.
 *
 * @author Michał Sadkowski
 * @version 1.0
 */
public class SudokuException extends Exception {

    /**
     * Creates a new exception with a specific error message.
     *
     * @param message The detailed error message.
     */
    public SudokuException(String message) {
        super(message);
    }
}