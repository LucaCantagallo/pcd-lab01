package pcd.common.provaRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServer extends UnicastRemoteObject implements ChatService {

    protected ChatServer() throws RemoteException {
        super();
    }

    @Override
    public String sendMessage(String message) throws RemoteException {
        System.out.println("Messaggio ricevuto dal client: " + message);
        return "Server: Ricevuto il tuo messaggio - " + message;
    }

    public static void main(String[] args) {
        try {
            // Creiamo un'istanza del server
            ChatServer server = new ChatServer();

            // Creiamo il registro RMI sulla porta 1099 (default)
            Registry registry = LocateRegistry.createRegistry(1099);

            // Registriamo il servizio con il nome "ChatService"
            registry.rebind("ChatService", server);

            System.out.println("Server RMI avviato...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

