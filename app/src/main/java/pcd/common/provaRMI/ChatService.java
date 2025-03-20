package pcd.common.provaRMI;

import pcd.ass03.part2.part2B.model.Grid;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    String sendMessage(String message) throws RemoteException;

    Grid getGrid() throws RemoteException;
}
