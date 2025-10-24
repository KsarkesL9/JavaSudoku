// pl.polsl.michal.sadkowski.java1.sudoku.view.SudokuGUI.java
package pl.polsl.michal.sadkowski.java1.sudoku.view;

import pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController;
import pl.polsl.michal.sadkowski.java1.sudoku.exceptions.InvalidInputException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher; 
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionListener; 
import java.util.ArrayList;
import java.util.List;

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

    // Pola dla elementów interfejsu (dla celów FocusTraversalPolicy)
    private JButton undoButton;
    private JButton clearCellButton;
    private JButton restartButton;
    private JButton newGameButton;
    private final List<Component> traversalOrder = new ArrayList<>();


    /**
     * Implementacja niestandardowej polityki przechodzenia fokusu, która kontroluje kolejność 
     * w pętli: Sudoku Cells -> Przyciski -> Sudoku Cells.
     */
    private class CustomFocusTraversalPolicy extends FocusTraversalPolicy {
        
        private final List<Component> order;

        public CustomFocusTraversalPolicy(List<Component> order) {
            this.order = order;
        }

        @Override
        public Component getFirstComponent(Container container) {
            return order.get(0);
        }

        @Override
        public Component getLastComponent(Container container) {
            return order.get(order.size() - 1);
        }

        @Override
        public Component getComponentAfter(Container container, Component component) {
            int index = order.indexOf(component);
            if (index == -1) return getFirstComponent(container); 
            // Cykliczne zawijanie (ostatni element -> pierwszy element)
            int nextIndex = (index + 1) % order.size(); 
            return order.get(nextIndex);
        }

        @Override
        public Component getComponentBefore(Container container, Component component) {
            int index = order.indexOf(component);
            if (index == -1) return getLastComponent(container);
            // Cykliczne zawijanie (pierwszy element -> ostatni element)
            int previousIndex = (index - 1 + order.size()) % order.size(); 
            return order.get(previousIndex);
        }

        @Override
        public Component getDefaultComponent(Container container) {
            return getFirstComponent(container);
        }
    }


    /**
     * Creates the main GUI window.
     */
    public SudokuGUI() {
        super("Sudoku");
        initUI();
        // Ustawienie niestandardowej polityki przejścia po elementach
        this.setFocusTraversalPolicy(new CustomFocusTraversalPolicy(traversalOrder));
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

        // --- 1. Dodanie komórek Sudoku do listy traversalOrder ---
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                 // Pobieramy pola z BoardPanel i dodajemy do listy w kolejności wiersz-kolumna
                 traversalOrder.add(boardPanel.getCells()[r][c]);
            }
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerLabel = new JLabel("Czas: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(timerLabel);
        controlPanel.add(timerPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Number buttons (1-9 only)
        JPanel numberButtonPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        for (int i = 1; i <= 9; i++) {
            JButton numButton = new JButton(String.valueOf(i));
            numButton.setFont(new Font("Arial", Font.BOLD, 16));
            numButton.setFocusable(true); // Włączamy tabulację dla przycisków
            int num = i;
            numButton.addActionListener(e -> {
                 int r = boardPanel.getSelectedRow();
                 int c = boardPanel.getSelectedCol();
                 // Delegation: View -> Controller (0-based)
                 if (controller != null) controller.handleCellInput(r, c, String.valueOf(num));
            });
            numberButtonPanel.add(numButton);
            traversalOrder.add(numButton); // Dodanie przycisku do kolejności
        }
        
        // Filler button to keep grid consistent (no '0' button anymore)
        Component filler = Box.createRigidArea(new Dimension(0, 0));
        numberButtonPanel.add(filler);
        filler.setFocusable(false); // Filler nie powinien być focusowalny

        controlPanel.add(numberButtonPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Action buttons (muszą być zainicjalizowane przed dodaniem do traversalOrder)
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        undoButton = new JButton("Cofnij");
        undoButton.setFocusable(true);
        undoButton.addActionListener(e -> { if (controller != null) controller.undoLastMove(); }); 
        actionButtonPanel.add(undoButton);
        traversalOrder.add(undoButton); // Dodanie przycisku do kolejności

        clearCellButton = new JButton("Wyczyść Pole");
        clearCellButton.setFocusable(true);
        clearCellButton.addActionListener(e -> {
            int r = boardPanel.getSelectedRow();
            int c = boardPanel.getSelectedCol();
            if (controller != null && r != -1 && c != -1) {
                controller.clearSelectedCell(r, c);
            }
        });
        actionButtonPanel.add(clearCellButton);
        traversalOrder.add(clearCellButton); // Dodanie przycisku do kolejności

        restartButton = new JButton("Restart");
        restartButton.setFocusable(true);
        restartButton.addActionListener(e -> { if (controller != null) controller.restartGame(); });
        actionButtonPanel.add(restartButton);
        traversalOrder.add(restartButton); // Dodanie przycisku do kolejności

        newGameButton = new JButton("Nowa gra");
        newGameButton.setFocusable(true);
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
        traversalOrder.add(newGameButton); // Dodanie przycisku do kolejności

        controlPanel.add(actionButtonPanel);

        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Ponieważ użyliśmy CustomFocusTraversalPolicy, nie potrzebujemy już 
        // globalnego KeyEventDisptacher do ręcznego przełączania się między celami
        // (chociaż ten dispatcher nadal odpowiada za obsługę TYPED klawiszy, nie nawigacji).

        // Register global keyboard listener for cell input (digits)
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (controller == null) return false;
                if (e.getID() == KeyEvent.KEY_TYPED && boardPanel.hasSelection()) {
                    char keyChar = e.getKeyChar();
                    
                    if (Character.isDigit(keyChar)) {
                        try {
                            if (keyChar == '0') {
                                throw new InvalidInputException(
                                    "Wprowadzono nieprawidÅ‚owy znak: " + keyChar + ". Cyfra '0' nie moÅ¼e byÄ‡ wpisana. UÅ¼yj Delete/Backspace/WyczyÅ›Ä‡ Pole, aby wyczyÅ›ciÄ‡."
                                );
                            }
                            String value = String.valueOf(keyChar);
                            int r = boardPanel.getSelectedRow();
                            int c = boardPanel.getSelectedCol();
                            
                            controller.handleCellInput(r, c, value); 
                            e.consume();
                            return true;
                        } catch (InvalidInputException ex) {
                             controller.handleInputValidationError(ex.getMessage());
                             e.consume();
                             return true;
                        }
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