package pl.polsl.michal.sadkowski.java1.sudoku.exceptions;

/**
 * Custom unchecked exception for invalid user input (e.g., non-digit characters) 
 * when trying to set a cell value.
 *
 * @author Michał Sadkowski
 * @version 1.0
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Creates a new exception with a specific error message.
     *
     * @param message The detailed error message.
     */
    public InvalidInputException(String message) {
        super(message);
    }
}