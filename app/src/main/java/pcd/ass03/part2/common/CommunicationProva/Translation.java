package pcd.ass03.part2.common.CommunicationProva;

public class Translation {

    private static final String SEPARATOR = ",";


    public static String togetherGmR(String gameMatrix, String riddle) {
        return gameMatrix + SEPARATOR + riddle;
    }

    public static String getGameMatrix(String message) {
        return message.split(SEPARATOR)[0];
    }

    public static String getRiddle(String message) {
        return message.split(SEPARATOR)[1];
    }
}
