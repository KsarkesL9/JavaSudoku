import pl.polsl.michal.sadkowski.java1.sudoku.view.SudokuGUI;
import javax.swing.SwingUtilities;

/**
 * Main class for Sudoku app.
 * Uruchamia wersję GUI.
 *
 * @author Michał Sadkowski
 * @version 1.2 
 */
public class Sudoku {

    /**
     * Entry point of the Sudoku application.
     * Uruchamia graficzny interfejs użytkownika.
     *
     * @param args command-line arguments (obecnie ignorowane przez GUI).
     */
    public static void main(String[] args) {
        // Uruchom GUI w wątku dystrybucji zdarzeń Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuGUI();
            }
        });

    }
}