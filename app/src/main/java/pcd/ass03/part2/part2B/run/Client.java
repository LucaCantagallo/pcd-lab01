package pcd.ass03.part2.part2B.run;

import pcd.ass03.part2.part2B.controller.StartController;
import pcd.ass03.part2.part2B.model.RMI;
import pcd.ass03.part2.part2B.view.StartView;
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

            RMI user = new RMI("2");
            UserCallbackImpl callback = new UserCallbackImpl(user);
            server.registerCallback(callback);


            StartView view = new StartView(user.getUsername());
            new StartController(view, user);
            view.setVisible(true);

            //rmi.createGrid("pippo");
            //System.out.println("griglia creata");
            //Grid grid = rmi.getGridByGameCode("pippo");
            //System.out.println("griglia ottenuta");
            //System.out.println(grid.getGameMatrix() + " " + grid.getRiddle());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
