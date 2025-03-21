package pcd.ass03.part2.part2B.model.remote;

import java.awt.*;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserCallback extends Remote {
    void onGridCreated() throws RemoteException;
    void onGridUpdate(String gameCode) throws RemoteException;
    void onCellSelected(String gameCode, int row, int col, Color color) throws RemoteException;
    void onCellUnselected(String gameCode, int row, int col) throws RemoteException;
}
