package pl.polsl.michal.sadkowski.java1.sudoku;
//import pl.polsl.michal.sadkowski.java1.sudoku.controller.AppController;

/**
 * Main class for Sudoku app.
 *
 * @author Michał Sadkowski
 * @version 1.1
 */
public class Sudoku {

 /**
 * Entry point of the Sudoku application.
 *
 * <p>If no arguments are provided, or the arguments are invalid in count or format,
 * the application starts an interactive REPL using System.in / System.out.</p>
 *
 * <p><b>Usage (non-interactive modes):</b></p>
 * <pre>
 *   java -jar Sudoku.jar print
 *   java -jar Sudoku.jar clear
 *   java -jar Sudoku.jar get &lt;row&gt; &lt;col&gt;
 *   java -jar Sudoku.jar set &lt;row&gt; &lt;col&gt; &lt;value&gt;
 * </pre>
 *
 * <p><b>Parameter order and formats:</b></p>
 * <ul>
 *   <li><code>args[0]</code> — <b>command</b>: one of <code>print</code>, <code>clear</code>, <code>get</code>, <code>set</code>.</li>
 *   <li><code>args[1]</code> — <b>row</b>: integer in range <code>1..9</code> (required for <code>get</code> and <code>set</code>).</li>
 *   <li><code>args[2]</code> — <b>col</b>: integer in range <code>1..9</code> (required for <code>get</code> and <code>set</code>).</li>
 *   <li><code>args[3]</code> — <b>value</b>: integer in range <code>0..9</code> (required only for <code>set</code>;
 *       <code>0</code> means "clear this cell").</li>
 * </ul>
 *
 * <p><b>Examples:</b></p>
 * <pre>
 *   java -jar Sudoku.jar print
 *   java -jar Sudoku.jar get 3 7
 *   java -jar Sudoku.jar set 5 5 9
 *   java -jar Sudoku.jar set 1 2 0   // clears cell (1,2)
 * </pre>
 *
 * <p>Invalid numbers (non-integers or out of the allowed ranges) are rejected with an error message
 * and the board remains unchanged.</p>
 *
 * @param args command-line arguments in the exact order described above.
 */


    public static void main(String[] args) {
        new pl.polsl.michal.sadkowski.java1.sudoku.controller.AppController().run(args);
    }
}