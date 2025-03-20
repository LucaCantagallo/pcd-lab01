package pcd.ass03.part2.part2B.model.remote;

import pcd.ass03.part2.part2B.model.Grid;
import java.awt.*;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameServer extends Remote, Serializable {

    void registerCallback(UserCallback userCallback) throws RemoteException;

    void createGrid(String gameCode) throws RemoteException;

    void updateGrid(String gameCode, int row, int col, int value) throws RemoteException;


    void selectCell(String gameCode, int row, int col, Color color) throws RemoteException;

    void unselectCell(String gameCode, int row, int col) throws RemoteException;

    Boolean existsGrid(String gameCode) throws RemoteException;

    Grid getGridByGameCode(String gameCode) throws RemoteException;

    List<String> getGameCodeList() throws RemoteException;
}
