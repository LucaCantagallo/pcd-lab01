package pcd.ass03.part2.common.CommunicationProva;

import java.io.IOException;

public class HandlerMessageDBGameCode {
    private static Rabbit rabbit;

    public static void initialize(Rabbit rabbit) {
        HandlerMessageDBGameCode.rabbit = rabbit;
        try {
            HandlerMessageDBGameCode.rabbit.sendGlobalGameCodes("");
        } catch (IOException e) {
            System.out.println("Non è stata inizializzata correttamente la lista dei gamecode!");
        }
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
        try {
            motherString = rabbit.receiveGlobalGameCodes();
        } catch (IOException e) {
            System.out.println("Non è stata ricevuta la lista dei gamecode");;
        }
        sendMotherString(motherString);
        return motherString;
    }

    public static void sendMotherString(String message){
        try {
            rabbit.sendGlobalGameCodes(message);
        } catch (IOException e) {
            System.out.println("Non è stato possibile aggiornare la lista dei gamecode");
        }
    }




}
