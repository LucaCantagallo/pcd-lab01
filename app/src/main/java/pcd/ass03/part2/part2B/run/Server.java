package pcd.ass03.part2.part2B.run;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject {

    public Server() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            // Creiamo un'istanza del server
            Server server = new Server();

            // Creiamo il registro RMI sulla porta 1099 (default)
            Registry registry = LocateRegistry.createRegistry(1099);

            // Registriamo il servizio con il nome "ChatService"
            registry.rebind("GameServer", server);

            System.out.println("Server RMI avviato...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
