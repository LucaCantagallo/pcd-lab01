package pcd.ass03.part2.common.CommunicationProva;

import java.io.IOException;

public class HandlerMessageDBGameCode {
    private static Rabbit rabbit;

    public static void initialize(Rabbit rabbit) {
        HandlerMessageDBGameCode.rabbit = rabbit;
    }

    public static boolean isPresent(String gamecode) {
        String motherString = getMotherString();
        String[] lines = motherString.split("\n");
        for (String line : lines) {
            if (line.equals(gamecode)) {
                return true;
            }
        }
        return false;
    }



    public static void addGameCode(String gamecode) {
        String motherString = getMotherString();
        if (!isPresent(gamecode)) {
            sendMotherString(gamecode + "\n" + motherString);
        } else {
            sendMotherString(motherString);
        }
    }

    public static void removeGameCode(String gamecode) {
        String[] lines = getMotherString().split("\n");
        StringBuilder newMotherString = new StringBuilder();
        for (String line : lines) {
            if (!line.equals(gamecode)) {
                newMotherString.append(line).append("\n");
            }
        }
        sendMotherString(newMotherString.toString().trim());
    }

    public static String getMotherString() {
        String motherString="";
        motherString = rabbit.receiveGlobalGameCodes();
        sendMotherString(motherString);
        return motherString;
    }

    public static void sendMotherString(String message){
        rabbit.sendGlobalGameCodes(message);
    }




}
