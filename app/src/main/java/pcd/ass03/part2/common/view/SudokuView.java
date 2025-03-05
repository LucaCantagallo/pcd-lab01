package pcd.ass03.part2.common.view;

import pcd.ass03.part2.common.sudoku.Cell;
import pcd.ass03.part2.common.sudoku.Grid;
import pcd.ass03.part2.common.sudoku.SudokuInsertChecker;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SudokuView extends JFrame {

    private final JTextField[][] textFields; // Array per gestire le JTextField
    private final SudokuInsertChecker insertChecker; // Oggetto per controllare gli inserimenti

    public SudokuView() {
        // Impostazioni della finestra principale
        setTitle("Sudoku");
        setSize(600, 700); // Aumento altezza per il codice sudoku
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Cambiamo layout per più controllo

        Grid sudokuGrid = new Grid("password"); // Qui crea sempre nuovi sudoku PER ORA
        insertChecker = new SudokuInsertChecker(sudokuGrid);
        System.out.println(sudokuGrid.getGm().toString());

        // Pannello per la griglia
        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        textFields = new JTextField[9][9];

        // Creiamo la griglia
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(60, 60));
                textField.setFont(new Font("Arial", Font.PLAIN, 18));
                textField.setHorizontalAlignment(JTextField.CENTER);

                Cell cell = sudokuGrid.getCell(row, col);

                if (cell.isShowed()) {
                    textField.setText(cell.getValue().map(String::valueOf).orElse(""));
                    textField.setEditable(false);
                } else {
                    textField.setText("");
                    textField.setEditable(true);
                }

                // Bordo sottile di default
                Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
                textField.setBorder(defaultBorder);

                // Bordi spessi ogni 3 righe e colonne
                if (row == 2 || row == 5) {
                    Border thickBottomBorder = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK);
                    textField.setBorder(BorderFactory.createCompoundBorder(textField.getBorder(), thickBottomBorder));
                }
                if (col == 2 || col == 5) {
                    Border thickRightBorder = BorderFactory.createMatteBorder(0, 0, 0, 3, Color.BLACK);
                    textField.setBorder(BorderFactory.createCompoundBorder(textField.getBorder(), thickRightBorder));
                }

                if(!cell.isShowed()) {
                    // Listener per l'inserimento
                    final int rowIndex = row;
                    final int colIndex = col;

                    textField.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            char c = e.getKeyChar();

                            if (Character.isDigit(c)) {
                                int insertedValue = Character.getNumericValue(c);
                                boolean isCorrect = insertChecker.checkInsert(rowIndex, colIndex, insertedValue);

                                if (isCorrect) {
                                    textField.setText(String.valueOf(insertedValue));
                                    textField.setEditable(false);
                                    if (insertChecker.checkWinning()) {
                                        JOptionPane.showMessageDialog(null, "Vittoria!", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Valore errato!", "Errore", JOptionPane.ERROR_MESSAGE);
                                    e.consume();
                                }
                            } else {
                                e.consume();
                            }
                        }
                    });
                }

                textFields[row][col] = textField;
                gridPanel.add(textField);
            }
        }

        // Etichetta codice sudoku
        JLabel codeLabel = new JLabel("Codice Sudoku: "+sudokuGrid.getPassword(), SwingConstants.CENTER);
        codeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Aggiungere elementi alla finestra
        add(gridPanel, BorderLayout.CENTER);
        add(codeLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

}
