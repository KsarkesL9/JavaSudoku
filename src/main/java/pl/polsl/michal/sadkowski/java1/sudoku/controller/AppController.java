// pl.polsl.michal.sadkowski.java1.sudoku.controller.AppController.java

package pl.polsl.michal.sadkowski.java1.sudoku.controller;

import pl.polsl.michal.sadkowski.java1.sudoku.model.SudokuGame;
import pl.polsl.michal.sadkowski.java1.sudoku.exceptions.SudokuException;
import pl.polsl.michal.sadkowski.java1.sudoku.view.ConsoleView;

/**
 * Simple controller:
 * - if have args -> do task and finish
 * - if no args -> simple REPL (print, set, get, clear, exit)
 *
 * @author Michał Sadkowski
 * @version 1.2 (fit to 0-based indexing model)
 */
public class AppController {
    /** The Sudoku game state. */
    private final SudokuGame game = new SudokuGame("guest");
    /** The console view for show output and take input. */
    private final ConsoleView view = new ConsoleView();

    /**
     * Run application logic.
     *
     * @param args command-line args to do one task, or null/empty for REPL mode.
     */
    public void run(String[] args) {
        if (args != null && args.length > 0) {
            handleCommand(args);
        } else {
            repl();
        }
    }

    /**
     * Do one command from command-line args.
     *
     * @param parts array of command parts (command, row, col, value).
     */
    private void handleCommand(String[] parts) {
        String cmd = parts[0].toLowerCase();

        try {
            if (cmd.equals("print")) {
                view.display(game.getBoard());
                return;
            }
            if (cmd.equals("clear")) {
                game.getBoard().clear();
                view.display("Board cleaned.");
                return;
            }
            if (cmd.equals("get")) {
                if (parts.length < 3) {
                    view.display("How to use: get <row 1-9> <col 1-9>");
                    return;
                }
                Integer r = view.tryParse(parts[1]), c = view.tryParse(parts[2]);
                if (r == null || c == null || r < 1 || r > 9 || c < 1 || c > 9) {
                    view.display("Not a correct number argument for get (must be 1-9).");
                    return;
                }
                int val = game.getBoard().getCell(r - 1, c - 1); 
                view.display(String.format("Cell (%d,%d) = %d", r, c, val));
                return;
            }
            if (cmd.equals("set")) {
                if (parts.length < 4) {
                    view.display("How to use: set <row 1-9> <col 1-9> <value 0-9>");
                    return;
                }
                Integer r = view.tryParse(parts[1]), c = view.tryParse(parts[2]), v = view.tryParse(parts[3]);
                if (r == null || c == null || v == null || r < 1 || r > 9 || c < 1 || c > 9 || v < 0 || v > 9) {
                    view.display("Not a correct number value for set (r, c must be 1-9; v must be 0-9).");
                    return;
                }
                game.getBoard().setCell(r - 1, c - 1, v);
                view.display("OK");
                return;
            }
            view.display("Unknown command. Use: print, set, get, clear");

        } catch (SudokuException e) {
            view.display("Error: " + e.getMessage());
        } catch (Exception e) {
            view.display("An bad error happen: " + e.getMessage());
        }
    }

    /**
     * Start the console loop (REPL).
     */
    private void repl() {
        view.display("Simple console sudoku editor 9x9");
        view.display("Commands: print | set r c v | get r c | clear | exit");

        while (true) {
            String line = view.prompt("cmd>");
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase();

            try {
                if (cmd.equals("exit")) {
                    view.display("Finish.");
                    return;
                } else if (cmd.equals("print")) {
                    view.display(game.getBoard());
                } else if (cmd.equals("clear")) {
                    game.getBoard().clear();
                    view.display("Board cleaned.");
                } else if (cmd.equals("get")) {
                    int r, c;
                    if (parts.length >= 3) {
                        r = Integer.parseInt(parts[1]);
                        c = Integer.parseInt(parts[2]);
                    } else {
                        r = view.askInt("Enter row 1-9:");
                        c = view.askInt("Enter column 1-9:");
                    }
                    if (r < 1 || r > 9 || c < 1 || c > 9) {
                        view.display("Error: Row/column must be 1-9.");
                        continue;
                    }
                    int val = game.getBoard().getCell(r - 1, c - 1); 
                    view.display(String.format("Cell (%d,%d) = %d", r, c, val));
                } else if (cmd.equals("set")) {
                    int r, c, v;
                    if (parts.length >= 4) {
                        r = Integer.parseInt(parts[1]);
                        c = Integer.parseInt(parts[2]);
                        v = Integer.parseInt(parts[3]);
                    } else {
                        r = view.askInt("Enter row 1-9:");
                        c = view.askInt("Enter column 1-9:");
                        v = view.askIntAllowZero("Enter value 0-9 (0 = empty):");
                    }
                    if (r < 1 || r > 9 || c < 1 || c > 9) {
                        view.display("Error: Row/column must be 1-9.");
                        continue;
                    }
                    game.getBoard().setCell(r - 1, c - 1, v);
                    view.display("OK");
                } else {
                    view.display("Unknown command. Use: print, set, get, clear, exit");
                }
            } catch (NumberFormatException e) {
                view.display("Error: Input is not a correct number value.");
            } catch (SudokuException e) {
                view.display("Error: " + e.getMessage());
            } catch (Exception e) {
                view.display("An bad error happen: " + e.getMessage());
            }
        }
    }
}