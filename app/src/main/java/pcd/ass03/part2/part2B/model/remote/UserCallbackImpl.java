package pcd.ass03.part2.part2B.model.remote;

import pcd.ass03.part2.part2B.model.RMI;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserCallbackImpl extends UnicastRemoteObject implements UserCallback {

    private final RMI user;

    public UserCallbackImpl(RMI user) throws RemoteException {
        this.user = user;
    }

    @Override
    public void onGridCreated() throws RemoteException {
        user.notifyGridCreated();
    }

    @Override
    public void onGridUpdate(String gameCode) throws RemoteException {
        user.notifyGridUpdated(gameCode);
    }

    @Override
    public void onCellSelected(String gameCode, int row, int col, Color color) throws RemoteException {
        user.notifyCellSelected(gameCode, row, col, color);
    }

    @Override
    public void onCellUnselected(String gameCode, int row, int col) throws RemoteException {
        user.notifyCellUnselect(gameCode, row, col);
    }

    @Override
    public void onGridSubmitted(String gameCode, String userId) throws RemoteException {
        user.notifyGridCompleted(gameCode, userId);
    }
}
