// pl.polsl.michal.sadkowski.java1.sudoku.view.SudokuGUI.java
package pl.polsl.michal.sadkowski.java1.sudoku.view;

import pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher; 
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionListener; 

/**
 * The main View class for the Sudoku GUI application.
 * It extends JFrame and implements the {@link pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater} 
 * interface to receive updates from the Controller.
 *
 * @author Michał Sadkowski
 * @version 1.0
 */
public class SudokuGUI extends JFrame implements SudokuGUIController.GUIUpdater {

    private BoardPanel boardPanel;
    private JLabel timerLabel;
    private SudokuGUIController controller; 

    /**
     * Creates the main GUI window.
     */
    public SudokuGUI() {
        super("Sudoku");
        initUI();
    }
    
    /** * Injects the Controller into the View (Dependency Injection).
     *
     * @param controller The Sudoku GUI Controller instance.
     */
    public void setController(SudokuGUIController controller) {
        this.controller = controller;
        this.boardPanel.setController(controller); 
    }
    
    /**
     * Initializes all graphical components of the main window.
     */
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        boardPanel = new BoardPanel(null); 
        add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerLabel = new JLabel("Czas: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(timerLabel);
        controlPanel.add(timerPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Number buttons
        JPanel numberButtonPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        for (int i = 1; i <= 9; i++) {
            JButton numButton = new JButton(String.valueOf(i));
            numButton.setFont(new Font("Arial", Font.BOLD, 16));
            numButton.setFocusable(false);
            int num = i;
            numButton.addActionListener(e -> {
                 int r = boardPanel.getSelectedRow();
                 int c = boardPanel.getSelectedCol();
                 // Delegation: View -> Controller (0-based)
                 if (controller != null) controller.handleCellInput(r, c, String.valueOf(num));
            });
            numberButtonPanel.add(numButton);
        }
        JButton clearButton = new JButton("0");
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setFocusable(false);
        clearButton.addActionListener(e -> {
            int r = boardPanel.getSelectedRow();
            int c = boardPanel.getSelectedCol();
            // Delegation: View -> Controller (clear cell)
            if (controller != null) controller.handleCellInput(r, c, "");
        });
        numberButtonPanel.add(clearButton);

        controlPanel.add(numberButtonPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Action buttons
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton undoButton = new JButton("Cofnij");
        undoButton.setFocusable(false);
        undoButton.addActionListener(e -> { if (controller != null) controller.undoLastMove(); }); 
        actionButtonPanel.add(undoButton);

        JButton restartButton = new JButton("Restart");
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> { if (controller != null) controller.restartGame(); });
        actionButtonPanel.add(restartButton);

        JButton newGameButton = new JButton("Nowa gra");
        newGameButton.setFocusable(false);
        newGameButton.addActionListener(e -> {
            String[] difficulties = {"Łatwy", "Średni", "Trudny"};
            String selectedDifficulty = (String) JOptionPane.showInputDialog(
                    this,
                    "Wybierz poziom trudności:",
                    "Nowa gra",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    difficulties,
                    difficulties[0]
            );

            if (selectedDifficulty != null) {
                if (controller != null) controller.startNewGame(selectedDifficulty);
            } 
        });
        actionButtonPanel.add(newGameButton);

        controlPanel.add(actionButtonPanel);

        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Register global keyboard listener for cell input (digits)
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (controller == null) return false;
                if (e.getID() == KeyEvent.KEY_TYPED && boardPanel.hasSelection()) {
                    char keyChar = e.getKeyChar();
                    if (Character.isDigit(keyChar)) {
                        String value = String.valueOf(keyChar);
                        int r = boardPanel.getSelectedRow();
                        int c = boardPanel.getSelectedCol();
                        
                        controller.handleCellInput(r, c, value); 
                        
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Implements the {@link pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater#setTimerText(java.lang.String)} method.
     * Sets the displayed text of the timer label.
     * * @param text The new timer text.
     */
    @Override
    public void setTimerText(String text) {
        timerLabel.setText(text);
    }

    /**
     * Implements the {@link pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater#setCellValue(int, int, java.lang.String)} method.
     * Updates the text value of a specific cell in the board panel.
     *
     * @param row The 0-based row index.
     * @param col The 0-based column index.
     * @param value The text value to set.
     */
    @Override
    public void setCellValue(int row, int col, String value) {
        boardPanel.setCellValue(row, col, value); 
    }
    
    /**
     * Implements the {@link pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater#updateCellBorder(int, int)} method.
     * Updates the visual selection/border of a specific cell in the board panel.
     *
     * @param row The 0-based row index.
     * @param col The 0-based column index.
     */
    @Override
    public void updateCellBorder(int row, int col) {
        boardPanel.selectCell(row, col); 
    }
    
    /**
     * Implements the {@link pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater#clearBoardGUI()} method.
     * Clears all cells on the GUI board.
     */
    @Override
    public void clearBoardGUI() {
        boardPanel.clearBoardGUI();
    }
    
    /**
     * Implements the {@link pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater#showWinMessage(java.lang.String)} method.
     * Displays a congratulatory message upon winning, including the elapsed time.
     *
     * @param time The formatted time taken to complete the game.
     */
    @Override
    public void showWinMessage(String time) {
        JOptionPane.showMessageDialog(this,
                    "Gratulacje! Ukończyłeś Sudoku w czasie " + time + "!",
                    "Wygrana!",
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Implements the {@link pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater#showInfoMessage(java.lang.String)} method.
     * Displays a standard informational message.
     *
     * @param message The message to display.
     */
    @Override
    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    /**
     * Implements the {@link pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater#showErrorMessage(java.lang.String)} method.
     * Displays a critical error message.
     *
     * @param message The error message to display.
     */
    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Błąd aplikacji", JOptionPane.ERROR_MESSAGE);
    }
}