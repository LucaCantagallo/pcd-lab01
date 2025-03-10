package pcd.ass03.part2.common.communicationOldVersion;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.Map;

public class RabbitMQCommunicationService implements CommunicationService {
    private static final String EXCHANGE_NAME = "sudoku_exchange";
    private Connection connection;
    private Map<String, Channel> channelMap;

    public RabbitMQCommunicationService() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channelMap = new ConcurrentHashMap<>();
    }

    private Channel getOrCreateChannel(String queueName) throws IOException {
        return channelMap.computeIfAbsent(queueName, key -> {
            try {
                Channel channel = connection.createChannel();
                channel.queueDeclare(key, true, false, false, null);
                return channel;
            } catch (IOException e) {
                throw new RuntimeException("Failed to create channel for queue: " + queueName, e);
            }
        });
    }

    private void sendMessage(String queueName, String message) throws IOException {
        Channel channel = getOrCreateChannel(queueName);
        channel.basicPublish("", queueName, null, message.getBytes());
    }

    @Override
    public void createGame(String gameCode) throws IOException {
        sendMessage("createGame_" + gameCode, gameCode);
    }

    @Override
    public void joinGame(String gameCode, String playerId) throws IOException {
        sendMessage("joinGame_" + gameCode, playerId);
    }

    @Override
    public void selectCell(String gameCode, String playerId, int x, int y) throws IOException {
        sendMessage("selectCell_" + gameCode, playerId + ":" + x + ":" + y);
    }

    @Override
    public void insertNumber(String gameCode, String playerId, int x, int y, int value) throws IOException {
        sendMessage("insertNumber_" + gameCode, playerId + ":" + x + ":" + y + ":" + value);
    }

    @Override
    public void leaveGame(String gameCode, String playerId) throws IOException {
        sendMessage("leaveGame_" + gameCode, playerId);
    }

    public void startListening(String gameCode) throws IOException {
        listenToQueue("createGame_" + gameCode, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Game created: " + message);
        });

        listenToQueue("joinGame_" + gameCode, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Player joined: " + message);
        });

        listenToQueue("selectCell_" + gameCode, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Cell selected: " + message);
        });

        listenToQueue("insertNumber_" + gameCode, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Number inserted: " + message);
        });

        listenToQueue("leaveGame_" + gameCode, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Player left: " + message);
        });
    }

    private void listenToQueue(String queueName, DeliverCallback callback) throws IOException {
        Channel channel = getOrCreateChannel(queueName);
        channel.basicConsume(queueName, true, callback, consumerTag -> {});
    }

    public void close() throws IOException, TimeoutException {
        for (Channel channel : channelMap.values()) {
            channel.close();
        }
        connection.close();
    }
}
