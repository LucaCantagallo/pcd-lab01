package pcd.ass03.part2.common.CommunicationProva;

import pcd.ass03.part2.common.sudoku.GameCodeDatabase;
import pcd.ass03.part2.common.sudoku.Grid;
import pcd.ass03.part2.common.view.HandlerSingleSudokuView;

public class HandlerSingleSudoku {

    private static final String SEPARATOR = ",";
    private static Rabbit rabbit;

    public static void initialize(Rabbit rabbit) {
        HandlerSingleSudoku.rabbit = rabbit;
    }

    public static Grid loadGrid(String gamecode, String fullMessage) {
        String gameMatrix = HandlerSingleSudoku.turnMessageToGameMatrixString(fullMessage);
        String riddle = HandlerSingleSudoku.turnMessageToRiddleString(fullMessage);
        return new Grid(gamecode, gameMatrix, riddle);
    }

    public static String generateMessage(String gameMatrix, String riddle) {
        return gameMatrix + SEPARATOR + riddle;
    }

    public static String generateMessage(Grid sudokuGrid) {
        return generateMessage(sudokuGrid.getGameMatrixToString(), sudokuGrid.getRiddleToString());
    }

    public static String turnMessageToGameMatrixString(String message) {
        return message.split(SEPARATOR)[0];
    }

    public static String turnMessageToRiddleString(String message) {
        return message.split(SEPARATOR)[1];
    }

    public static void sendMessage(Grid sudokuGrid) {
        String message = HandlerSingleSudoku.generateMessage(sudokuGrid.getGameMatrixToString(), sudokuGrid.getRiddleToString());
        rabbit.sendMessage(sudokuGrid.getGamecode(), message);
    }

    public static String receiveMessage(String gamecode) {
        return rabbit.receiveMessage(gamecode);
    }

    public static void startListening(String gamecode) {
        rabbit.listenForUpdates(gamecode, callback -> {
            System.out.println("Eseguito callback, nuovo Sudoku ricevuto.");
            //System.out.println(callback);
            rabbit.sendMessage(gamecode, callback);
            Grid sudokuGrid = HandlerSingleSudoku.loadGrid(gamecode, callback);
            GameCodeDatabase.addGameCode(gamecode, sudokuGrid);
            HandlerSingleSudokuView.updateGridUI(sudokuGrid);
        });
    }

    public static void updateMessage(Grid sudokuGrid, String message) {
        rabbit.updateMessageSudoku(sudokuGrid.getGamecode(), message);
    }
}
