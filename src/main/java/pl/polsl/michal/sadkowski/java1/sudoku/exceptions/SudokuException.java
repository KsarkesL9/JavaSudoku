package pl.polsl.michal.sadkowski.java1.sudoku.exceptions;

/**
 * Custom checked exception for Sudoku model bad operation.
 * This exception is thrown when user break Sudoku rule.
 *
 * @author Michał Sadkowski
 * @version 1.0
 */
public class SudokuException extends Exception {

    /**
     * Make new exception with message.
     *
     * @param message the bad message.
     */
    public SudokuException(String message) {
        super(message);
    }
}