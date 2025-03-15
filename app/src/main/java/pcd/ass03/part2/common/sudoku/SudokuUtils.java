package pcd.ass03.part2.common.sudoku;

import pcd.ass03.part2.common.CommunicationProva.HandlerSingleSudoku;

import java.util.Optional;

public class SudokuUtils {

    // Metodo per verificare se l'inserimento è corretto
    public static boolean checkAndInsertValue(Grid sudokuGrid, int row, int col, int insertedValue) {
        Cell cell = sudokuGrid.getCell(row, col);

        // Confronta il valore inserito con il valore nascosto della cella
        if (cell.getHiddenValue() == insertedValue) {
            cell.setValue(Optional.of(insertedValue));

            //Riddle
            sudokuGrid.setRiddle(SudokuUtils.updateRiddle(sudokuGrid.getRiddleToString(), row, col, ""+insertedValue));
            //sudokuGrid.setValueRiddle(row, col, (byte) insertedValue);

            //Aggiornamenti
            GameCodeDatabase.addGameCode(sudokuGrid.getGamecode(), sudokuGrid);
            HandlerSingleSudoku.updateMessage(sudokuGrid);
            //System.out.println(HandlerSingleSudoku.generateMessage(sudokuGrid));
            return true; // Valore corretto
        }
        //Da qui in futuro potremmo contare gli errori
        return false; // Valore errato
    }

    public static void setFocus(Grid sudokuGrid, int row, int col, boolean visited){
        sudokuGrid.getCell(row,col).setVisited(visited);
        sudokuGrid.getCell(row,col).setVisitedByMe(visited);
    }

    public static String updateRiddle(String riddle, int row, int col, String newValue) {
        String[] lines = riddle.split("\n");  // Dividiamo la stringa in righe

        if (row < 0 || row >= lines.length || col < 0 || col >= lines[row].length()) {
            throw new IllegalArgumentException("Posizione fuori dai limiti!");
        }

        String currentCell = String.valueOf(lines[row].charAt(col));

        if (currentCell.equals("_")) { // Controlliamo che sia modificabile
            StringBuilder updatedLine = new StringBuilder(lines[row]);
            updatedLine.replace(col, col + 1, newValue); // Sostituisce il valore come String
            lines[row] = updatedLine.toString();
        } else {
            System.out.println("La cella non è modificabile!");
        }

        return String.join("\n", lines);  // Ricostruiamo l'intera stringa
    }

    public static boolean checkWinning(Grid sudokuGrid){
        return sudokuGrid.countEmpty()==0;
    }
}
