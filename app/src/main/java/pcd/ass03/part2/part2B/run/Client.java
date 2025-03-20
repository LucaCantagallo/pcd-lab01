package pcd.ass03.part2.part2B.run;

import pcd.ass03.part2.part2B.model.Grid;
import pcd.ass03.part2.part2B.model.RMI;
import pcd.ass03.part2.part2B.model.remote.GameServer;
import pcd.ass03.part2.part2B.model.remote.UserCallbackImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try {
            // Otteniamo il registro RMI sulla porta 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            GameServer server = (GameServer) registry.lookup("GameServer");
            System.out.println("ci siamo collegati al server!");

            RMI rmi = new RMI("1");
            UserCallbackImpl callback = new UserCallbackImpl(rmi);
            server.registerCallback(callback);
            rmi.createGrid("pippo");
            System.out.println("griglia creata");
            Grid grid = rmi.getGridByGameCode("pippo");
            System.out.println("griglia ottenuta");
            System.out.println(grid.getGameMatrix() + " " + grid.getRiddle());
            //TODO: implementare la logica del client
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
