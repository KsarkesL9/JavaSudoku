// pl.polsl.michal.sadkowski.java1.sudoku.Sudoku.java
package pl.polsl.michal.sadkowski.java1.sudoku;

import pl.polsl.michal.sadkowski.java1.sudoku.view.SudokuGUI;
import pl.polsl.michal.sadkowski.java1.sudoku.model.SudokuGame;
import pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController;

import javax.swing.SwingUtilities;

/**
 * Main class for Sudoku app.
 * Uruchamia wersję GUI, łącząc warstwy Modelu, Widoku i Kontrolera (MVC).
 *
 * @author Michał Sadkowski
 * @version 1.2 
 */
public class Sudoku {

    /**
     * Entry point of the Sudoku application.
     * Uruchamia graficzny interfejs użytkownika, stosując Dependency Injection (Wstrzykiwanie Zależności).
     *
     * @param args command-line arguments (obecnie ignorowane przez GUI).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Inicjalizacja Modelu
                SudokuGame model = new SudokuGame("GUI Player");
                
                // 2. Inicjalizacja Widoku (bez Controller, bo jeszcze go nie ma)
                SudokuGUI view = new SudokuGUI();
                
                // 3. Inicjalizacja Kontrolera (wstrzykujemy Model i referencję do View)
                SudokuGUIController controller = new SudokuGUIController(model, view);
                
                // 4. Wstrzyknięcie Kontrolera do Widoku (kończy cykl zależności)
                view.setController(controller); 
            }
        });
    }
}