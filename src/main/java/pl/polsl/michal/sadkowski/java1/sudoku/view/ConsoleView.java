// pl.polsl.michal.sadkowski.java1.sudoku.view.ConsoleView.java

package pl.polsl.michal.sadkowski.java1.sudoku.view;

import pl.polsl.michal.sadkowski.java1.sudoku.model.Board;
import java.util.Scanner;

/**
 * Console view for display Sudoku board and handle user input.
 *
 * @author Michał Sadkowski
 * @version 1.2 (try Parse refactor)
 */
public class ConsoleView {
    /** Scanner object for reading user input from console. */
    private final Scanner sc;

    /**
     * Make a ConsoleView and start Scanner.
     */
    public ConsoleView() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Show the Sudoku board.
     *
     * @param board The Board object to show.
     */
    public void display(Board board) {
        System.out.println(board.toString());
    }

    /**
     * Show simple text line to console.
     *
     * @param line The string to show.
     */
    public void display(String line) {
        System.out.println(line);
    }

    /**
     * Show a prompt and wait for user line.
     *
     * @param text The text prompt to show user.
     * @return The line text user write.
     */
    public String prompt(String text) {
        System.out.print(text + " ");
        String line = sc.nextLine();
        return line != null ? line.trim() : "";
    }

    /**
     * Ask user for a number (dla wiersza/kolumny).
     *
     * @param prompt The prompt message to show.
     * @return The number user write.
     * @throws NumberFormatException if the input is not a good number.
     */
    public int askInt(String prompt) {
        String line = prompt(prompt);
        return Integer.parseInt(line);
    }

    /**
     * Ask user for a number (dla wartości 0-9).
     *
     * @param prompt The prompt message to show.
     * @return The number user write.
     * @throws NumberFormatException if the input is not a good number.
     */
    public int askIntAllowZero(String prompt) {
        String line = prompt(prompt);
        return Integer.parseInt(line);
    }

    /**
     * Helper method to parse string to a number.
     *
     * @param s The string to parse.
     * @return The number or null if bad parse.
     */
    public Integer tryParse(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}