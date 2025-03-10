package pcd.ass03.part2.common.communicationOldVersion;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMICommunicationServiceImpl extends UnicastRemoteObject implements RMICommunicationService {
    protected RMICommunicationServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void createGame(String gameCode) throws RemoteException {
        System.out.println("Creating game: " + gameCode);
    }

    @Override
    public void joinGame(String gameCode, String playerId) throws RemoteException {
        System.out.println(playerId + " joined game: " + gameCode);
    }

    @Override
    public void selectCell(String gameCode, String playerId, int x, int y) throws RemoteException {
        System.out.println(playerId + " selected cell: " + x + "," + y + " in game: " + gameCode);
    }

    @Override
    public void insertNumber(String gameCode, String playerId, int x, int y, int value) throws RemoteException {
        System.out.println(playerId + " inserted " + value + " in cell: " + x + "," + y + " in game: " + gameCode);
    }

    @Override
    public void leaveGame(String gameCode, String playerId) throws RemoteException {
        System.out.println(playerId + " left game: " + gameCode);
    }
}
