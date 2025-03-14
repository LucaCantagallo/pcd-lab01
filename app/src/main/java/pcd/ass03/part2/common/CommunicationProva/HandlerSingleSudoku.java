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
        try {
            rabbit.sendMessage(sudokuGrid.getGamecode(), HandlerSingleSudoku.generateMessage(sudokuGrid.getGameMatrixToString(), sudokuGrid.getRiddleToString()));
        } catch (IOException e) {
            System.out.println("Sudoku non salvato correttamente!");
        }
    }

    public static String receiveMessage(String gamecode){
        String message;
        try {
            message = rabbit.receiveMessage(gamecode);
        } catch (IOException e) {
            message="";
        }
        return message;
    }

    public static Grid loadGrid(String gamecode, String fullMessage){
            String message = fullMessage;
            String gameMatrix = HandlerSingleSudoku.turnMessageToGameMatrixString(message);
            String riddle = HandlerSingleSudoku.turnMessageToRiddleString(fullMessage);
            System.out.println("RIDDLE"+riddle);
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
            Grid sudokuGrid = HandlerSingleSudoku.loadGrid(gamecode, callback);
            //System.out.println(HandlerSingleSudoku.generateMessage(sudokuGrid));
            GameCodeDatabase.addGameCode(gamecode, sudokuGrid);
            HandlerSingleSudokuView.updateGridUI(sudokuGrid);
            System.out.println("Aggiornata la griglia da Listening");
        });
    }

    public static void isOpeningConnection(String gamecode, Grid grid) {
        rabbit.updateMessageSudoku(gamecode, HandlerSingleSudoku.generateMessage(grid));
    }



    public static void updateMessage(Grid sudokuGrid){
        rabbit.updateMessageSudoku(sudokuGrid.getGamecode(), HandlerSingleSudoku.generateMessage(sudokuGrid));
    }

}
