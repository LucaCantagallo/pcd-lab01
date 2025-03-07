package pcd.ass03.part2.common.sudoku;

import java.util.HashSet;
import java.util.Set;

public class GameCodeDatabase {
    private static final Set<String> gameCodes = new HashSet<>();

    // Blocco statico per inizializzare i game code
    static {
        gameCodes.add("esempio");
        gameCodes.add("aaa");
        gameCodes.add("prova");
        gameCodes.add("gamecode");
    }

    // Metodo statico per verificare se un codice è valido
    public static boolean isPresentCode(String code) {
        return gameCodes.contains(code);
    }
}
