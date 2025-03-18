package pcd.ass03.part2.part2A.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import pcd.ass03.part2.part2A.controller.GridUpdateListener;
import pcd.ass03.part2.part2A.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Rabbit {
    private final String id;
    private final List<Grid> allGrids;
    private Channel channel;
    private static final String EXCHANGE_CREATE = "create";
    private static final String EXCHANGE_UPDATE = "update";
    private static final String EXCHANGE_SELECT = "select";
    private static final String EXCHANGE_UNSELECT = "unselect";
    private static final String EXCHANGE_REQUEST_GRID = "requestgrid";
    private static final String EXCHANGE_GET_GRID = "getgrid";
    private final List<GridUpdateListener> listeners;
    private final String color;

    public Rabbit(String id, String color) throws IOException, TimeoutException {
        this.id = id;
        this.color = color;
        this.allGrids = new ArrayList<>();
        this.listeners = new ArrayList<>();

        this.setupConnection();

        channel.exchangeDeclare(EXCHANGE_CREATE, "fanout");
        channel.exchangeDeclare(EXCHANGE_UPDATE, "fanout");
        channel.exchangeDeclare(EXCHANGE_SELECT, "fanout");
        channel.exchangeDeclare(EXCHANGE_UNSELECT, "fanout");
        channel.exchangeDeclare(EXCHANGE_REQUEST_GRID, "fanout");
        channel.exchangeDeclare(EXCHANGE_GET_GRID, "fanout");

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_CREATE, "");
        channel.basicConsume(queueName, true, addGridCallBack(), t -> {});

        String updateQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(updateQueueName, EXCHANGE_UPDATE, "");
        channel.basicConsume(updateQueueName, true, updateGridCallBack(), t -> {});

        String selectQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(selectQueueName, EXCHANGE_SELECT, "");
        channel.basicConsume(selectQueueName, true, selectCellCallBack(), t -> {});

        String unselectQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(unselectQueueName, EXCHANGE_UNSELECT, "");
        channel.basicConsume(unselectQueueName, true, unselectCellCallBack(), t -> {});

        String receiveGridQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(receiveGridQueueName, EXCHANGE_REQUEST_GRID, "");
        channel.basicConsume(receiveGridQueueName, true, requestGridCallBack(), t -> {});

        String getGridQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(getGridQueueName, EXCHANGE_GET_GRID, "");
        channel.basicConsume(getGridQueueName, true, getGridCallBack(), t -> {});

        requestGrid();
    }

    public String getColor() {
        return color;
    }

    void setupConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
    }

    public void addGridUpdateListener(GridUpdateListener listener){
        listeners.add(listener);
    }

    private void notifyGridCreated() {
        System.out.println("notifyGridCreated");
        for (GridUpdateListener listener : listeners) {
            listener.onGridCreated();
        }
    }

    private void notifyGridUpdated(String gamecode) {
        for (GridUpdateListener listener : listeners) {
            listener.onGridUpdated(gamecode);
        }
    }

    private void notifyCellSelected(int gridId, int row, int col, Color color, String idUser) {
        for (GridUpdateListener listener : listeners) {
            listener.onCellSelected(gridId, row, col, color, idUser);
        }
    }

    private void notifyCellUnselect(int gridId, int row, int col){
        for (GridUpdateListener listener : listeners) {
            listener.onCellUnselected(gridId, row, col);
        }
    }

    private void notifyGridCompleted(int gridId, String userId) {
        for (GridUpdateListener listener : listeners) {
            listener.onGridCompleted(gridId, userId);
        }
    }

    public String getId() {
        return id;
    }

    public List<Grid> getAllGrids() {
        return allGrids;
    }

    public void createGrid(String gameCode) throws IOException, TimeoutException {
        int gridId = allGrids.size() + 1;
        Grid grid = new Grid(gridId, gameCode);
        allGrids.add(grid);

        publishGrid(grid);
        notifyGridCreated();
    }

    public void publishGrid(Grid grid) throws IOException {
        String message = grid.getId() + " " + Utils.toString(grid);
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_CREATE, "", null, message.getBytes(StandardCharsets.UTF_8));
    }

    public void updateGrid(int gridId, int row, int col, int value) throws IOException {
        String message = gridId + " " + row + " " + col + " " + value;
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_UPDATE, "", null, message.getBytes(StandardCharsets.UTF_8));
    }

    public void selectCell(int gridId, int row, int col) throws IOException {
        String message = gridId + " " + row + " " + col + " " + color + " " + id;
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_SELECT, "", null, message.getBytes(StandardCharsets.UTF_8));
    }

    public void unselectCell(int gridId, int row, int col) throws IOException {
        String message = gridId + " " + row + " " + col;
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_UNSELECT, "", null, message.getBytes(StandardCharsets.UTF_8));
    }

    public void requestGrid() throws IOException {
        String message = "request";
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_REQUEST_GRID, "", null, message.getBytes(StandardCharsets.UTF_8));
    }

    public void getGrid() throws IOException {
        String message = allGrids.stream()
                .map(grid -> grid.getId() + " " + Utils.toString(grid)) // Converte ogni Grid in una stringa
                .collect(Collectors.joining("-"));
        System.out.println(message);
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_GET_GRID, "", null, message.getBytes(StandardCharsets.UTF_8));
    }

    public int cellHiddenValue(int gridId, int row, int col) {
        System.out.println(gridId);
        System.out.println(allGrids.get(gridId-1).getHiddenValue(row, col));
        return this.allGrids.get(gridId-1).getHiddenValue(row, col);
    }

    private void ensureChannelIsOpen() throws IOException {
        if (channel == null || !channel.isOpen()) {
            try {
                setupConnection();
            } catch (TimeoutException e) {
                throw new IOException("Failed to reopen channel", e);
            }
        }
    }

    private String findGameCodeByGridId(int gridId) {
        return allGrids.stream()
                .filter(grid -> grid.getId() == gridId)
                .map(Grid::getGameCode)
                .findFirst().get();
    }

    private DeliverCallback updateGridCallBack(){
        System.out.println("updateGridCallBack");
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] parts = message.split(" ");
            int gridId = Integer.parseInt(parts[0]);
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            int value = Integer.parseInt(parts[3]);
            try {
                allGrids.get(gridId - 1).setCellValue(row, col, value);
                notifyGridUpdated(findGameCodeByGridId(gridId));
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Grid not found");
            }
        };
    }

    private DeliverCallback requestGridCallBack(){
        System.out.println("requestGridCallBack");
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("ricevo richiesta");
            if (!allGrids.isEmpty()){
                System.out.println("invio griglie");
                getGrid();
            }
        };
    }

    private DeliverCallback getGridCallBack(){
        System.out.println("getGridCallBack");
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("ricevo griglie");
            if (allGrids.isEmpty()){
                System.out.println(message);
                String[] parts = message.split("-");
                System.out.println(Arrays.toString(parts));
                for (String part : parts){
                    System.out.println(part);
                    Grid receivedGrid = Utils.fromString(part);
                    allGrids.add(receivedGrid);
                }
            }
        };
    }

    private DeliverCallback addGridCallBack(){
        System.out.println("addGridCallBack");
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Grid receivedGrid = Utils.fromString(message);

            System.out.println(receivedGrid.getGameMatrix() + " " + receivedGrid.getRiddle());
            if (allGrids.stream().noneMatch(grid -> grid.getId() == (receivedGrid.getId()))) {
                allGrids.add(receivedGrid);
                notifyGridCreated();
            }
        };
    }

    private DeliverCallback selectCellCallBack(){
        System.out.println("selectCellCallBack");
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            String[] parts = message.split(" ");
            int gridId = Integer.parseInt(parts[0]);
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            Color color = Utils.getColorByName(parts[3]);
            String idUser = parts[4];
            this.notifyCellSelected(gridId, row, col, color, idUser);
        };
    }

    private DeliverCallback unselectCellCallBack(){
        System.out.println("unselectCellCallBack");
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] parts = message.split(" ");
            int gridId = Integer.parseInt(parts[0]);
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            if (row == -1 && col == -1){
                this.notifyGridCompleted(gridId, parts[3]);
            } else {
                this.notifyCellUnselect(gridId, row, col);
            }
            this.notifyCellUnselect(gridId, row, col);
        };
    }

    public Grid getGrid(int index){
        return allGrids.get(index);
    }

    public Grid getGridByGameCode(String gameCode){
        return allGrids.stream().filter(grid -> grid.getGameCode().equals(gameCode)).findFirst().orElse(null);
    }



    public List<String> getGameCodeList(){
        return allGrids.stream().map(Grid::getGameCode).collect(Collectors.toList());
    }

    public boolean isPresent(String gamecode){
        return this.getGameCodeList().contains(gamecode);
    }

}