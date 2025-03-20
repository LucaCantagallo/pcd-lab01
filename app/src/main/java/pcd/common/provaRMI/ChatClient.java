package pcd.common.provaRMI;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

