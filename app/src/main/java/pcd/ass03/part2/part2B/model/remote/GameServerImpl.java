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

public class GameServerImpl implements GameServer {

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
        System.out.println(users.size());
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
        System.out.println("select cell");
        for (UserCallback callback : users) {
            try {
                System.out.println("faccio call back");
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

    @Override
    public void submitGrid(String gameCode, String userId) throws RemoteException {
        for (UserCallback callback : users) {
            try {
                callback.onGridSubmitted(gameCode, userId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }


}