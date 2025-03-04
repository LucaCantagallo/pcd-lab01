package pcd.ass03.part2.common.view;

import pcd.ass03.part2.common.sudoku.Cell;
import pcd.ass03.part2.common.sudoku.Grid;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SudokuView extends JFrame {

    private Grid sudokuGrid;
    private JTextField[][] textFields; // Array per gestire le JTextField

    public SudokuView() {
        // Impostazioni della finestra principale
        setTitle("Sudoku");
        setSize(600, 600); // Impostiamo la dimensione della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 9)); // Definiamo una griglia 9x9
        sudokuGrid = new Grid(); // Qui crea sempre nuovi sudoku PER ORA

        // Inizializziamo l'array di JTextField
        textFields = new JTextField[9][9];

        // Aggiungiamo le JTextField per ogni cella della griglia
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(60, 60)); // Impostiamo la dimensione della casella di testo
                textField.setFont(new Font("Arial", Font.PLAIN, 18)); // Impostiamo il font
                textField.setHorizontalAlignment(JTextField.CENTER); // Centrato il testo

                // Otteniamo la Cell corrispondente nella logica del Sudoku
                Cell cell = sudokuGrid.getCell(row, col);

                // Se la cella è mostrata (cioè contiene un valore fisso), disabilitiamo il campo di testo
                if (cell.isShowed()) {
                    textField.setText(cell.getValue().map(String::valueOf).orElse(""));
                    textField.setEditable(false); // Disabilitiamo la modifica
                } else {
                    textField.setText(""); // Campo vuoto per valori modificabili
                    textField.setEditable(true); // Permettiamo l'inserimento
                }

                // Creiamo un bordo sottile di default
                Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 1); // Bordo sottile

                // Aggiungiamo il bordo sottile
                textField.setBorder(defaultBorder);

                // Aggiungiamo il bordo spesso solo per le righe 3-4 e 6-7
                if (row == 2 || row == 5) {
                    Border currentBorder = textField.getBorder(); // Otteniamo il bordo corrente (sottile)
                    Border thickBottomBorder = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK); // Bordo spesso
                    textField.setBorder(BorderFactory.createCompoundBorder(currentBorder, thickBottomBorder)); // Combiniamo il bordo sottile con il bordo spesso
                }

                // Aggiungiamo il bordo spesso solo per le colonne 3-4 e 6-7
                if (col == 2 || col == 5) {
                    Border currentBorder = textField.getBorder(); // Otteniamo il bordo corrente (sottile)
                    Border thickRightBorder = BorderFactory.createMatteBorder(0, 0, 0, 3, Color.BLACK); // Bordo spesso
                    textField.setBorder(BorderFactory.createCompoundBorder(currentBorder, thickRightBorder)); // Combiniamo il bordo sottile con il bordo spesso
                }

                // Aggiungiamo il JTextField all'array e alla finestra
                textFields[row][col] = textField;
                add(textField);
            }
        }

        // Rende la finestra visibile
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuView());
    }
}
