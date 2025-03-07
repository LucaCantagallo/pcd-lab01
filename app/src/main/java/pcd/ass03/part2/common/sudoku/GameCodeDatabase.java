package pcd.ass03.part2.common.sudoku;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameCodeDatabase {
    private static final Map<String, Grid> gameCodes = new HashMap<>();


    // Metodo statico per verificare se un codice è valido
    public static boolean isPresentCode(String gameCode) {
        return gameCodes.containsKey(gameCode);
    }

    public static void addGameCode(String gameCode, Grid grid) {
        gameCodes.put(gameCode, grid);
    }

    public static Grid getGrid(String gameCode) {
        return gameCodes.get(gameCode);
    }

    public static Set<String> getCodes() {
        return gameCodes.keySet();
    }
}
