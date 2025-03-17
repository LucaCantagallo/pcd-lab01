package pcd.ass03.part2.common.CommunicationProva;

import pcd.ass03.part2.common.sudoku.GameCodeDatabase;
import pcd.ass03.part2.common.sudoku.Grid;
import pcd.ass03.part2.common.view.HandlerSingleSudokuView;

import java.io.IOException;

public class HandlerSingleSudoku {

    private static final String SEPARATOR = ",";
    private static Rabbit rabbit;

    public static void initialize(Rabbit rabbit){
        HandlerSingleSudoku.rabbit = rabbit;
    }

    public static void sendMessage(Grid sudokuGrid){
        String message = HandlerSingleSudoku.generateMessage(sudokuGrid.getGameMatrixToString(), sudokuGrid.getRiddleToString());
        System.out.println("message:");
        System.out.println(message);
        rabbit.sendMessage(sudokuGrid.getGamecode(), message);
    }

    public static String receiveMessage(String gamecode){
        String message;
        message = rabbit.receiveMessage(gamecode);
        System.out.println("[DEBUG] Messaggio letto dalla coda principale: " + message);
        rabbit.sendMessage(GameCodeDatabase.getGrid(gamecode).getGamecode(), message);
        return message;
    }

    public static Grid loadGrid(String gamecode, String fullMessage){
            String message = fullMessage;
            String gameMatrix = HandlerSingleSudoku.turnMessageToGameMatrixString(message);
            String riddle = HandlerSingleSudoku.turnMessageToRiddleString(message);
            return new Grid(gamecode, gameMatrix, riddle);
    }

    public static String generateMessage(String gameMatrix, String riddle) {
        return gameMatrix + SEPARATOR + riddle;
    }

    public static String generateMessage(Grid sudokuGrid){
        return generateMessage(sudokuGrid.getGameMatrixToString(), sudokuGrid.getRiddleToString());
    }

    public static String turnMessageToGameMatrixString(String message) {
        return message.split(SEPARATOR)[0];
    }

    public static String turnMessageToRiddleString(String message) {
        return message.split(SEPARATOR)[1];
    }

    public static void startListening(String gamecode) {
        rabbit.listenForUpdates(gamecode, callback -> {
            System.out.println("Eseguito callback, nuovo Sudoku ricevuto: " + callback);

            // Carica la nuova griglia aggiornata dall'update ricevuto
            Grid sudokuGrid = HandlerSingleSudoku.loadGrid(gamecode, callback);

            // Aggiorna il database locale del Sudoku
            GameCodeDatabase.addGameCode(gamecode, sudokuGrid);

            // 🔹 Proviamo a stampare prima di inviare il messaggio
            System.out.println("Invio il messaggio aggiornato nella coda principale: " + callback);
            System.out.println("[DEBUG] Sudoku salvato in GameCodeDatabase: " + sudokuGrid.getGameMatrixToString());
            rabbit.sendMessage(gamecode, callback);

            // Aggiorna la UI
            HandlerSingleSudokuView.updateGridUI(sudokuGrid);
            System.out.println("Aggiornata la griglia da Listening e salvata nella coda principale");
        });
    }






    public static void updateMessage(Grid sudokuGrid, String message){
        rabbit.updateMessageSudoku(sudokuGrid.getGamecode(), message);
    }

}
