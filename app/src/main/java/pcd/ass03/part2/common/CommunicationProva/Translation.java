package pcd.ass03.part2.common.CommunicationProva;

public class Translation {


    public static String togetherGmR(String gameMatrix, String riddle) {
        return gameMatrix + "," + riddle;
    }

    public static String getGameMatrix(String message) {
        return message.split(",")[0];
    }

    public static String getRiddle(String message) {
        return message.split(",")[1];
    }
}
