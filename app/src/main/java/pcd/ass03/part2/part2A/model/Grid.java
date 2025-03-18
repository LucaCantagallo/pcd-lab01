package pcd.ass03.part2.part2A.model;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;
import de.sfuhrm.sudoku.Riddle;
import pcd.ass03.part2.common.sudoku.GameCodeDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Grid {

    private final int id;
    private final Cell[][] grid;
    private boolean completed = false;
    private final String gameMatrix;
    private final String riddle;
    private final String gameCode;

    public Grid(int id, String gameCode) {
        this.id = id;
        this.gameCode = gameCode;
        GameMatrix gm;
        Riddle r;
        gm = Creator.createFull();
        r = Creator.createRiddle(gm);
        gameMatrix = gm.toString();
        riddle = r.toString();
        grid = new Cell[9][9];
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                grid[row][col] = new Cell(!r.getWritable(row,col)? Optional.of((int) gm.get(row,col)) : Optional.empty(), gm.get(row,col));
            }
        }
    }

    public Grid(int id, String gm, String r, String gameCode){
        this.id = id;
        this.gameMatrix=gm;
        this.riddle=gm;
        this.gameCode = gameCode;
        List<String> gmList = new ArrayList<>();
        List<String> rList = new ArrayList<>();
        // Itera attraverso ogni carattere della stringa
        System.out.println("p1");
        for (int i = 0; i < gm.length(); i++) {
            char gmChar = gm.charAt(i);
            char rChar = r.charAt(i);
            // Aggiungi il carattere alla lista se non è uno spazio, una nuova linea, o SEPARATOR
            if (gmChar != ' ' && gmChar != '\n') {
                gmList.add(String.valueOf(gmChar));
            }
            if (rChar != ' ' && rChar != '\n') {
                rList.add(String.valueOf(rChar));
            }
        }
        System.out.println("p2");
        List<Integer> gmMappedList = gmList.stream()
                .map(Integer::parseInt)  // Converte in Integer
                .collect(Collectors.toList());
        List<Optional<Integer>> rMappedList = new ArrayList<>();

        System.out.println("p3");
        for (String s : rList) {
            if (s.matches("\\d+")) { // Controlla se la stringa è un numero
                rMappedList.add(Optional.of(Integer.parseInt(s)));
            } else {
                rMappedList.add(Optional.empty());
            }
        }

        System.out.println("p4");
        int count=0;
        grid = new Cell[9][9];

        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                this.grid[row][col] = new Cell(rMappedList.get(count), gmMappedList.get(count));
                count++;
            }
        }

    }

    public void setCellValue(int row, int col, int value) {
        if(!isValidValue(value)){
            return;
        }
        grid[row][col].setValue(value);
    }

    private boolean isValidValue(int value){
        return value >= 0 && value <= 9;
    }

    public int getId() {
        return id;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted() {
        this.completed = true;
    }

    public int getHiddenValue(int row, int col) {
        return grid[row][col].getHiddenValue();
    }

    public String getGameMatrix() {
        return gameMatrix;
    }

    public String getRiddle() {
        return riddle;
    }

    public String getGameCode() {
        return gameCode;
    }
}
