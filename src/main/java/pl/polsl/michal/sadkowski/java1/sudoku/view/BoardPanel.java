// pl.polsl.michal.sadkowski.java1.sudoku.view.BoardPanel.java
package pl.polsl.michal.sadkowski.java1.sudoku.view;

import pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

public class BoardPanel extends JPanel {

    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private final JTextField[][] cells;
    private int selectedRow = -1;
    private int selectedCol = -1;
    
    private SudokuGUIController controller; // Umożliwia setController


    private final Color backgroundColor = Color.WHITE;
    private final Color selectedColor = new Color(173, 216, 230);
    private final Color subgridBorderColor = Color.BLACK;
    private final Color cellBorderColor = Color.LIGHT_GRAY;
    private final Font cellFont = new Font("Arial", Font.BOLD, 20);
    private final Border thickBorder = new LineBorder(subgridBorderColor, 2);
    private final Border selectedBorder = new LineBorder(Color.BLUE, 2);


    public BoardPanel(SudokuGUIController controller) { 
        this.controller = controller; 
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        cells = new JTextField[GRID_SIZE][GRID_SIZE];
        initializeBoard();
        setupArrowKeyNavigation();
    }
    
    /** Umożliwia wstrzyknięcie Kontrolera. */
    public void setController(SudokuGUIController controller) {
         this.controller = controller;
    }

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

                // DELEGACJA: Kliknięcie i Focus idzie do Kontrolera
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
                     public void focusLost(FocusEvent e) { /* Czysty View */ }
                });

                // DELEGACJA: Wprowadzanie klawiszem idzie do Kontrolera
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
    
    // Logika wizualna (WIDOKOWA)
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
    
    // Metoda polecana przez Kontroler do zmiany wartości wyświetlanej
    public void setCellValue(int row, int col, String value) { 
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            cells[row][col].setText(value);
        }
    }
    
    // Metoda polecana przez Kontroler do czyszczenia View
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
    
    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }
    
    // Logika nawigacji (WIDOKOWA)
    private void setupArrowKeyNavigation() {
        // Kod ten pozostaje bez zmian, manipuluje wyłącznie stanem 'selectedRow/Col'
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(-1, 0);
            }
        });
        // ... (reszta skrótów klawiszowych dla DOWN, LEFT, RIGHT) ...
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
    
    public boolean hasSelection() {
        return selectedRow != -1 && selectedCol != -1;
    }
}