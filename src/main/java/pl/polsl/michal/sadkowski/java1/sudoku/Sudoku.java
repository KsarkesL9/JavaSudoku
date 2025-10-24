// pl.polsl.michal.sadkowski.java1.sudoku.Sudoku.java
package pl.polsl.michal.sadkowski.java1.sudoku;

import pl.polsl.michal.sadkowski.java1.sudoku.view.SudokuGUI;
import pl.polsl.michal.sadkowski.java1.sudoku.model.SudokuGame;
import pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController;

import javax.swing.SwingUtilities;

/**
 * Main class for the Sudoku application.
 * It starts the GUI version, connecting the Model, View, and Controller layers (MVC).
 *
 * @author Michał Sadkowski
 * @version 1.2 
 */
public class Sudoku {

    /**
     * The main entry point of the Sudoku application.
     * Starts the graphical user interface using Dependency Injection for MVC components.
     *
     * @param args command-line arguments (currently ignored by the GUI).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SudokuGame model = new SudokuGame("GUI Player");
                SudokuGUI view = new SudokuGUI();
                SudokuGUIController controller = new SudokuGUIController(model, view);
                view.setController(controller); 
            }
        });
    }
}