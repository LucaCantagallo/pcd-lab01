package pcd.ass03.part2.common.CommunicationProva;

public class HandlerMessageDBGameCode {
    private static String motherString = "";

    public static boolean isPresent(String gamecode) {
        String[] lines = motherString.split("\n");
        for (String line : lines) {
            if (line.equals(gamecode)) {
                return true;
            }
        }
        return false;
    }

    public static void addGameCode(String gamecode) {
        if (!isPresent(gamecode)) {
            motherString = gamecode + "\n" + motherString;
        }
    }

    public static void removeGameCode(String gamecode) {
        String[] lines = motherString.split("\n");
        StringBuilder newMotherString = new StringBuilder();
        for (String line : lines) {
            if (!line.equals(gamecode)) {
                newMotherString.append(line).append("\n");
            }
        }
        motherString = newMotherString.toString().trim();
    }

    public static String getMotherString() {
        return motherString;
    }
}
