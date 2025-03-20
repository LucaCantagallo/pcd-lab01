package pcd.common.provaRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    String sendMessage(String message) throws RemoteException;
}
