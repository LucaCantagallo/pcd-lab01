package pcd.ass03.part2.part2B.run;


import pcd.ass03.part2.part2B.model.remote.GameServer;
import pcd.ass03.part2.part2B.model.remote.GameServerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {


    public static void main(String[] args) {
        try {


            LocateRegistry.createRegistry(1099);
            // Crea l'oggetto remoto
            GameServer gameManager = new GameServerImpl();

            // Esporta l'oggetto remoto su una porta libera
            GameServer stub = (GameServer) UnicastRemoteObject.exportObject(gameManager, 0);

            // Ottieni il registro RMI
            Registry registry = LocateRegistry.getRegistry();

            // Registra il servizio nel registro RMI
            registry.rebind("GameService", stub);

            System.out.println("🚀 Server RMI avviato sulla porta 1099...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
