// pl.polsl.michal.sadkowski.java1.sudoku.view.ConsoleView.java

package pl.polsl.michal.sadkowski.java1.sudoku.view;

import pl.polsl.michal.sadkowski.java1.sudoku.model.Board;
import java.util.Scanner;

/**
 * Console view responsible for displaying the Sudoku board and handling raw user input 
 * from the console.
 *
 * @author Michał Sadkowski
 * @version 1.2 (try Parse refactor)
 */
public class ConsoleView {
    /** Scanner object for reading user input from the console. */
    private final Scanner sc;

    /**
     * Creates a ConsoleView instance and initializes the Scanner object.
     */
    public ConsoleView() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Displays the current state of the Sudoku board to the console.
     *
     * @param board The Board object to display.
     */
    public void display(Board board) {
        System.out.println(board.toString());
    }

    /**
     * Displays a simple text line to the console.
     *
     * @param line The string content to show.
     */
    public void display(String line) {
        System.out.println(line);
    }

    /**
     * Displays a prompt and waits for the user to enter a line of text.
     *
     * @param text The prompt message to show the user.
     * @return The trimmed line of text entered by the user, or an empty string if null.
     */
    public String prompt(String text) {
        System.out.print(text + " ");
        String line = sc.nextLine();
        return line != null ? line.trim() : "";
    }

    /**
     * Prompts the user for input and attempts to parse it as an integer (typically for row/column 1-9).
     *
     * @param prompt The prompt message to show.
     * @return The number entered by the user.
     * @throws NumberFormatException if the input cannot be correctly parsed as an integer.
     */
    public int askInt(String prompt) {
        String line = prompt(prompt);
        return Integer.parseInt(line);
    }

    /**
     * Prompts the user for input and attempts to parse it as an integer (typically for value 0-9).
     *
     * @param prompt The prompt message to show.
     * @return The number entered by the user.
     * @throws NumberFormatException if the input cannot be correctly parsed as an integer.
     */
    public int askIntAllowZero(String prompt) {
        String line = prompt(prompt);
        return Integer.parseInt(line);
    }

    /**
     * Helper method to safely parse a string to an Integer.
     *
     * @param s The string to attempt to parse.
     * @return The parsed Integer, or {@code null} if the parsing fails due to a bad format.
     */
    public Integer tryParse(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}