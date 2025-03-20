package pcd.ass03.part2.part2B.model.remote;


import pcd.ass03.part2.part2B.model.Grid;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServerImpl implements GameServer {

    private final Map<String, Grid> allGrids;
    private final List<UserCallback> callbacks;

    public GameServerImpl() {
        this.allGrids = new HashMap<>();
        this.callbacks = new ArrayList<>();
    }

    @Override
    public void registerCallback(UserCallback userCallback) {
        callbacks.add(userCallback);
    }

    @Override
    public synchronized void createGrid(String gameCode) {
        int gridId = allGrids.size();
        Grid grid = new Grid(gridId, gameCode);
        allGrids.put(gameCode, grid);
        for (UserCallback callback : callbacks) {
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
        for (UserCallback callback : callbacks) {
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
        for (UserCallback callback : callbacks) {
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
        for (UserCallback callback : callbacks) {
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
    public Grid getGrid(int gridId) {
        return null;
    } // TODO: se serve implementare

    @Override
    public Grid getGridByGameCode(String gameCode) {
        return allGrids.get(gameCode);
    }
}