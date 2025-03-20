package pcd.ass03.part2.part2B.run;

import pcd.ass03.part2.part2B.model.remote.GameServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try {
            // Otteniamo il registro RMI sulla porta 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            GameServer server = (GameServer) registry.lookup("GameServer");
            System.out.println("ci siamo collegati al server!");

            //TODO: implementare la logica del client
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
