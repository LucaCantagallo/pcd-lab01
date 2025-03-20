package pcd.ass03.part2.part2B.model;

import pcd.ass03.part2.part2B.model.remote.GameServer;
import pcd.ass03.part2.part2B.model.remote.GameServerImpl;
import pcd.ass03.part2.part2B.utils.Utils;
import pcd.ass03.part2.part2B.controller.GridUpdateListener;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RMI {

    private final String id;
    private final List<GridUpdateListener> listeners;
    private final Color color;
    private final GameServer gameServer;

    public RMI(String id) throws RemoteException {
        this.id = id;
        this.listeners = new ArrayList<>();
        this.color = Utils.convertStringToColor(Utils.generateRandomColor());
        this.gameServer = new GameServerImpl();
    }

    public Color getColor() {
        return this.color;
    }

    public String getId() {
        return this.id;
    }


    public void createGrid(String gameCode) throws IOException, TimeoutException {
        gameServer.createGrid(gameCode);
    }


    public void updateGrid(String gameCode, int row, int col, int value) throws IOException {
        gameServer.updateGrid(gameCode, row, col, value);
    }

    public void selectCell(String gamecode, int row, int col) throws IOException {
        gameServer.selectCell(gamecode, row, col, color);
    }

    public void unselectCell(String gamecode, int row, int col) throws IOException {
        gameServer.unselectCell(gamecode, row, col);
    }


    public int cellHiddenValue(String gamecode, int row, int col) throws RemoteException {
        return gameServer.getGridByGameCode(gamecode).getHiddenValue(row, col);
    }

    public Grid getGridByGameCode(String gameCode) throws RemoteException {
        return gameServer.getGridByGameCode(gameCode);
    }

    public List<String> getGameCodeList() throws RemoteException {
        return gameServer.getGameCodeList();
    }

    public boolean isPresent(String gamecode) throws RemoteException {
        return gameServer.existsGrid(gamecode);
    }
}
