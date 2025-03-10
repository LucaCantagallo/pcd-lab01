package pcd.ass03.part2.common.communicationOldVersion;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMICommunicationService extends Remote {
    void createGame(String gameCode) throws RemoteException;
    void joinGame(String gameCode, String playerId) throws RemoteException;
    void selectCell(String gameCode, String playerId, int x, int y) throws RemoteException;
    void insertNumber(String gameCode, String playerId, int x, int y, int value) throws RemoteException;
    void leaveGame(String gameCode, String playerId) throws RemoteException;
}