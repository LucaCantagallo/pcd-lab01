package pcd.ass03.part2.part2B.run;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try {
            // Otteniamo il registro RMI sulla porta 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Server server = (Server) registry.lookup("GameServer");
            System.out.println("ci siamo collegati al server!");

            //TODO: implementare la logica del client
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
