// pl.polsl.michal.sadkowski.java1.sudoku.view.BoardPanel.java
package pl.polsl.michal.sadkowski.java1.sudoku.view;

import pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

/**
 * Custom JPanel component to display the 9x9 Sudoku grid.
 * It handles cell display, selection, mouse/keyboard input delegation to the controller, and arrow key navigation.
 *
 * @author Michał Sadkowski
 * @version 1.0
 */
public class BoardPanel extends JPanel {

    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private final JTextField[][] cells;
    private int selectedRow = -1;
    private int selectedCol = -1;
    
    private SudokuGUIController controller; 

    private final Color backgroundColor = Color.WHITE;
    private final Color selectedColor = new Color(173, 216, 230);
    private final Color subgridBorderColor = Color.BLACK;
    private final Font cellFont = new Font("Arial", Font.BOLD, 20);
    private final Border selectedBorder = new LineBorder(Color.BLUE, 2);


    /**
     * Creates a new BoardPanel and initializes its components.
     *
     * @param controller The main GUI controller (can be null for later injection).
     */
    public BoardPanel(SudokuGUIController controller) { 
        this.controller = controller; 
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        cells = new JTextField[GRID_SIZE][GRID_SIZE];
        initializeBoard();
        setupArrowKeyNavigation();
    }
    
    /** * Allows injection of the Controller.
     *
     * @param controller The Sudoku GUI Controller instance.
     */
    public void setController(SudokuGUIController controller) {
         this.controller = controller;
    }

    /**
     * Initializes all 81 JTextField cells with styles and event listeners.
     */
    private void initializeBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new JTextField();
                JTextField cell = cells[row][col];
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(cellFont);
                cell.setBackground(backgroundColor);
                cell.setEditable(true); 
                cell.setFocusable(true);

                cell.setBorder(getCompositeBorder(row, col));

                final int r = row;
                final int c = col;

                // Delegation: Mouse click and focus events go to the Controller
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        selectCell(r, c);
                        if (controller != null) controller.handleCellClick(r, c); 
                    }
                });

                cell.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                       selectCell(r, c);
                       if (controller != null) controller.handleCellClick(r, c); 
                    }
                     @Override
                     public void focusLost(FocusEvent e) { /* Pure View */ }
                });

                // Delegation: Key input goes to the Controller
                cell.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (controller == null) return;
                        char keyChar = e.getKeyChar();
                        if (Character.isDigit(keyChar) && keyChar != KeyEvent.VK_BACK_SPACE) {
                             e.consume(); 
                             String newValue = String.valueOf(keyChar);
                             controller.handleCellInput(r, c, newValue);

                        } else if (keyChar == KeyEvent.VK_BACK_SPACE || keyChar == KeyEvent.VK_DELETE || keyChar == '0') {
                             e.consume(); 
                             controller.handleCellInput(r, c, "");
                        } else {
                            e.consume();
                        }
                    }
                });
                
                add(cell);
            }
        }
        int preferredSize = 50 * GRID_SIZE;
        setPreferredSize(new Dimension(preferredSize, preferredSize));
    }
    
    /**
     * Creates a border for a cell, distinguishing thick borders for 3x3 subgrids 
     * and adding a highlight border if the cell is selected.
     *
     * @param row The 0-based row index.
     * @param col The 0-based column index.
     * @return The composite border for the cell.
     */
    private Border getCompositeBorder(int row, int col) {
         int top = 1, left = 1, bottom = 1, right = 1;
         if (row % SUBGRID_SIZE == 0) top = 2;
         if (col % SUBGRID_SIZE == 0) left = 2;
         if ((row + 1) % SUBGRID_SIZE == 0 ) bottom = 2;
         if ((col + 1) % SUBGRID_SIZE == 0 ) right = 2;

         Border subgridBorder = BorderFactory.createMatteBorder(top, left, bottom, right, subgridBorderColor);

        if (row == selectedRow && col == selectedCol) {
           return BorderFactory.createCompoundBorder(selectedBorder, subgridBorder);
        }

        return subgridBorder;
    }
    
    /**
     * Updates the visual state of the board by highlighting a newly selected cell 
     * and removing the highlight from the previously selected one. (View Logic)
     *
     * @param row The 0-based row index of the cell to select.
     * @param col The 0-based column index of the cell to select.
     */
    public void selectCell(int row, int col) {
        int previousSelectedRow = selectedRow;
        int previousSelectedCol = selectedCol;

        if (previousSelectedRow != -1 && previousSelectedCol != -1 &&
            (previousSelectedRow != row || previousSelectedCol != col))
        {
            cells[previousSelectedRow][previousSelectedCol].setBackground(backgroundColor);
            cells[previousSelectedRow][previousSelectedCol].setBorder(getCompositeBorder(previousSelectedRow, previousSelectedCol));
        }

        selectedRow = row;
        selectedCol = col;
        
        if (selectedRow != -1 && selectedCol != -1) {
            cells[selectedRow][selectedCol].setBackground(selectedColor);
            cells[selectedRow][selectedCol].setBorder(getCompositeBorder(selectedRow, selectedCol));
            cells[selectedRow][selectedCol].requestFocusInWindow();
        }
    }
    
    /**
     * Sets the displayed text value of a cell. This method is typically called by the Controller.
     *
     * @param row The 0-based row index (0-8).
     * @param col The 0-based column index (0-8).
     * @param value The text value to display.
     */
    public void setCellValue(int row, int col, String value) { 
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            cells[row][col].setText(value);
        }
    }
    
    /**
     * Clears all cells on the GUI board and resets the selection. This method is typically called by the Controller.
     */
    public void clearBoardGUI() {
         for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
               setCellValue(row, col, "");
            }
        }
         if (selectedRow != -1 && selectedCol != -1) {
            cells[selectedRow][selectedCol].setBackground(backgroundColor);
            cells[selectedRow][selectedCol].setBorder(getCompositeBorder(selectedRow, selectedCol));
            selectedRow = -1;
            selectedCol = -1;
        }
    }
    
    /**
     * Gets the currently selected row index.
     *
     * @return The 0-based selected row index, or -1 if no cell is selected.
     */
    public int getSelectedRow() {
        return selectedRow;
    }

    /**
     * Gets the currently selected column index.
     *
     * @return The 0-based selected column index, or -1 if no cell is selected.
     */
    public int getSelectedCol() {
        return selectedCol;
    }
    
    /**
     * Sets up keyboard shortcuts (arrow keys) for navigation on the board. (View Logic)
     */
    private void setupArrowKeyNavigation() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(-1, 0);
            }
        });
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(1, 0);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(0, -1);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(0, 1);
            }
        });
    }

    /**
     * Calculates the new selection based on the delta and calls {@link #selectCell(int, int)}.
     *
     * @param rowDelta Change in row index (-1, 0, or 1).
     * @param colDelta Change in column index (-1, 0, or 1).
     */
    private void moveSelection(int rowDelta, int colDelta) {
        if (selectedRow == -1 || selectedCol == -1) {
            selectCell(0, 0);
        } else {
            int newRow = selectedRow + rowDelta;
            int newCol = selectedCol + colDelta;

            if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE) {
                 selectCell(newRow, newCol);
            }
        }
    }
    
    /**
     * Checks if a cell is currently selected.
     *
     * @return {@code true} if a cell is selected, {@code false} otherwise.
     */
    public boolean hasSelection() {
        return selectedRow != -1 && selectedCol != -1;
    }
}