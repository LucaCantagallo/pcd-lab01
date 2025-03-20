package pcd.ass03.part2.part2B.model.remote;


import pcd.ass03.part2.part2B.model.Grid;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {

    private final Map<String, Grid> allGrids;
    private final List<UserCallback> users;

    public GameServerImpl() throws RemoteException {
        super();
        this.allGrids = new HashMap<>();
        this.users = new ArrayList<>();
    }

    @Override
    public void registerCallback(UserCallback userCallback) {
        users.add(userCallback);
    }

    @Override
    public synchronized void createGrid(String gameCode) {
        int gridId = allGrids.size();
        Grid grid = new Grid(gridId, gameCode);
        allGrids.put(gameCode, grid);
        System.out.println(allGrids.keySet());
        for (UserCallback callback : users) {
            try {
                callback.onGridCreated();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public synchronized void updateGrid(String gameCode, int row, int col, int value) {
        allGrids.get(gameCode).setCellValue(row, col, value);
        for (UserCallback callback : users) {
            try {
                callback.onGridUpdate(gameCode);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public synchronized void selectCell(String gameCode, int row, int col, Color color) {
        allGrids.get(gameCode).setColor(row, col, color);
        for (UserCallback callback : users) {
            try {
                callback.onCellSelected(gameCode, row, col, color);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public synchronized void unselectCell(String gameCode, int row, int col) {
        allGrids.get(gameCode).setColor(row, col, Color.WHITE);
        for (UserCallback callback : users) {
            try {
                callback.onCellUnselected(gameCode, row, col);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Boolean existsGrid(String gameCode) {
        return allGrids.containsKey(gameCode);
    }


    @Override
    public Grid getGridByGameCode(String gameCode) {
        return allGrids.get(gameCode);
    }

    @Override
    public List<String> getGameCodeList() throws RemoteException {
        return allGrids.keySet().stream().collect(Collectors.toList());
    }


}