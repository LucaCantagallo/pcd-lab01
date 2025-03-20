package pcd.common.provaRMI;

import pcd.ass03.part2.part2B.model.Grid;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatClient {
    public static void main(String[] args) {
        try {
            // Otteniamo il registro RMI sulla porta 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Cerchiamo il servizio registrato come "ChatService"
            ChatService stub = (ChatService) registry.lookup("ChatService");

            // Inviamo un messaggio al server
            String response = stub.sendMessage("Ciao Server!");
            System.out.println("Risposta dal server: " + response);
            Grid grid = stub.getGrid();
            System.out.println("Grid: " + grid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

