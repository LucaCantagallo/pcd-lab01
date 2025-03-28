package pcd.ass03.part2.part2B.model;

import pcd.ass03.part2.part2B.controller.GameController;
import pcd.ass03.part2.part2B.model.remote.GameServer;
import pcd.ass03.part2.part2B.model.remote.GameServerImpl;
import pcd.ass03.part2.part2B.model.remote.UserCallbackImpl;
import pcd.ass03.part2.part2B.utils.Utils;
import pcd.ass03.part2.part2B.controller.GridUpdateListener;

import java.awt.*;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMI implements Serializable {

    private final String username;
    private final List<GridUpdateListener> listeners;
    private final Color color;
    private final GameServer gameServer;

    public RMI(String username) throws RemoteException, NotBoundException {
        this.username = username;
        this.listeners = new ArrayList<>();
        this.color = Utils.convertStringToColor(Utils.generateRandomColor());


        // Ottiene il riferimento al registro RMI (Remote Method Invocation)
        // Il metodo getRegistry() senza parametri cerca il registro RMI sulla porta di default (1099) del localhost
        Registry registry = LocateRegistry.getRegistry();

        // Effettua il lookup del servizio "GameService" registrato nel registro RMI
        // Questo recupera un riferimento remoto all'oggetto GameServer, che può essere usato per invocare metodi remoti
        gameServer = (GameServer) registry.lookup("GameService");

        // Registra una callback dell'utente presso il server
        // L'oggetto UserCallbackImpl implementa un'interfaccia di callback che permette al server di notificare il client di eventi
        gameServer.registerCallback(new UserCallbackImpl(this));
    }

    public Color getColor() {
        return this.color;
    }

    public String getUsername() {
        return this.username;
    }


    //metodi per interagire con il server

    public void createGrid(String gameCode) {
        try {
            gameServer.createGrid(gameCode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateGrid(String gameCode, int row, int col, int value) {
        try {
            gameServer.updateGrid(gameCode, row, col, value);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectCell(String gamecode, int row, int col) {
        try {
            gameServer.selectCell(gamecode, row, col, color);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void unselectCell(String gamecode, int row, int col){
        try {
            gameServer.unselectCell(gamecode, row, col);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public int cellHiddenValue(String gamecode, int row, int col) {
        try {
            return gameServer.getGridByGameCode(gamecode).getHiddenValue(row, col);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Grid getGridByGameCode(String gameCode){
        try {
            return gameServer.getGridByGameCode(gameCode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getGameCodeList() {
        try {
            return gameServer.getGameCodeList();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPresent(String gamecode) {
        try {
            return gameServer.existsGrid(gamecode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyGridCreated() {
        for (GridUpdateListener listener : listeners) {
            listener.onGridCreated();
        }
    }

    public void notifyGridUpdated(String gameCode) {
        for (GridUpdateListener listener : listeners) {
            listener.onGridUpdated(gameCode);
        }
        if(getGridByGameCode(gameCode).isCompleted()){  //se la griglia è completata notifico tutti i listener
            notifyGridCompleted(gameCode, username);
        }
    }

    public void notifyCellSelected(String gameCode, int row, int col, Color color) {
        for (GridUpdateListener listener : listeners) {
            listener.onCellSelected(gameCode, row, col, color);
        }
    }

    public void notifyCellUnselect(String gameCode, int row, int col) {
        for (GridUpdateListener listener : listeners) {
            listener.onCellUnselected(gameCode, row, col);
        }
    }

    public void addGridUpdateListener(GridUpdateListener listener) {
        listeners.add(listener);
    }

    public void notifyGridCompleted(String gameCode, String userId)  {
        try {
            gameServer.submitGrid(gameCode, userId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        for (GridUpdateListener listener : listeners) {
            listener.onGridSubmitted(gameCode, userId);
        }
    }
}
