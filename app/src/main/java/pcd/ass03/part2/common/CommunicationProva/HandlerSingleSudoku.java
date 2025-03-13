package pcd.ass03.part2.common.CommunicationProva;

import pcd.ass03.part2.common.sudoku.GameCodeDatabase;
import pcd.ass03.part2.common.sudoku.Grid;
import pcd.ass03.part2.common.view.HandlerSingleSudokuView;
import pcd.ass03.part2.common.view.SudokuView;

import javax.swing.*;
import java.io.IOException;

public class HandlerSingleSudoku {

    private static final String SEPARATOR = ",";
    private static Rabbit rabbit;

    public static void initialize(Rabbit rabbit){
        HandlerSingleSudoku.rabbit = rabbit;
    }

    public static void sendMessage(Grid sudokuGrid){
        try {
            rabbit.sendMessage(sudokuGrid.getGamecode(), HandlerSingleSudoku.generateMessage(sudokuGrid.getGmMessage(), sudokuGrid.getRMessage()));
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

    public static void generateGrid(String gamecode, String fullMessage){
        if(fullMessage!=""){
            new Grid(gamecode, HandlerSingleSudoku.turnMessageToGameMatrixString(fullMessage), HandlerSingleSudoku.turnMessageToRiddleString(fullMessage));
        }
    }

    public static String generateMessage(String gameMatrix, String riddle) {
        return gameMatrix + SEPARATOR + riddle;
    }

    public static String turnMessageToGameMatrixString(String message) {
        return message.split(SEPARATOR)[0];
    }

    public static String turnMessageToRiddleString(String message) {
        return message.split(SEPARATOR)[1];
    }

    public static void startListening(String gamecode) {
        rabbit.listenForUpdates(gamecode, message -> {
            generateGrid(gamecode, message);
            HandlerSingleSudokuView.updateGridUI(GameCodeDatabase.getGrid(gamecode));
            System.out.println("Aggiornata la griglia con: " + message);
        });
    }



    public static void updateMessage(Grid sudokuGrid){
        rabbit.updateMessageSudoku(sudokuGrid.getGamecode(), HandlerSingleSudoku.generateMessage(sudokuGrid.getGmMessage(), sudokuGrid.getRMessage()));
    }

}
