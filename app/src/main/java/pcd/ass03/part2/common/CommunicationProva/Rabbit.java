package pcd.ass03.part2.common.CommunicationProva;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class Rabbit {
    private Connection connection;
    private Channel channel;
    private final String GLOBALGAMECODENAME = "GLOBALGAMECODE";

    public Rabbit() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.queueDeclare(this.GLOBALGAMECODENAME, true, false, false, null);
        } catch (IOException | TimeoutException e) {
            System.out.println("Errore nella connessione: " + e.getMessage());
        }
    }

    public String receiveGlobalGameCodes() {
        this.reconnect();
        try {
            GetResponse response = channel.basicGet(this.GLOBALGAMECODENAME, true);
            if (response == null) return "";
            return new String(response.getBody(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    public void sendGlobalGameCodes(String message) {
        this.reconnect();
        try {
            channel.queuePurge(this.GLOBALGAMECODENAME);
            channel.basicPublish("", this.GLOBALGAMECODENAME, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Errore nell'invio: " + e.getMessage());
        }
    }

    public String receiveMessage(String gamecode) {
        this.reconnect();
        String queueName = gamecode + "_queue";
        try {
            channel.queueDeclare(queueName, true, false, false, null);
            GetResponse response = channel.basicGet(queueName, false);
            if (response != null) {
                channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
                return new String(response.getBody(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.out.println("Errore nella ricezione: " + e.getMessage());
        }
        return "";
    }

    public void sendMessage(String gamecode, String message) {
        this.reconnect();
        String queueName = gamecode + "_queue";
        try {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queuePurge(queueName);
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Aggiornata "+queueName+ " con il seguente messaggio:");
            System.out.println(message);
        } catch (IOException e) {
            System.out.println("Errore nell'invio: " + e.getMessage());
        }
    }

    public void listenForUpdates(String gamecode, Consumer<String> callback) {
        this.reconnect();
        String exchangeName = gamecode + "_exchange";
        try {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, "");
            channel.basicConsume(queueName, true, (consumerTag, message) -> {
                callback.accept(new String(message.getBody(), StandardCharsets.UTF_8));
            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Errore nell'ascolto: " + e.getMessage());
        }
    }

    public void updateMessageSudoku(String gamecode, String update) {
        this.reconnect();
        String exchangeName = gamecode + "_exchange";
        try {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true);
            channel.basicPublish(exchangeName, "", null, update.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Errore nell'aggiornamento: " + e.getMessage());
        }
    }

    public void reconnect() {
        try {
            if (connection == null || !connection.isOpen()) {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                this.connection = factory.newConnection();
            }
            if (channel == null || !channel.isOpen()) {
                this.channel = connection.createChannel();
            }
        } catch (IOException | TimeoutException e) {
            System.err.println("[Rabbit] Errore nel riconnettersi: " + e.getMessage());
        }
    }

    public void close() throws IOException, TimeoutException {
        this.reconnect();
        if (channel != null) channel.close();
        if (connection != null) connection.close();
    }
}
