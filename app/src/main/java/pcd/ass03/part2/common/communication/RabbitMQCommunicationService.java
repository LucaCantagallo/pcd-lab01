package pcd.ass03.part2.common.communication;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RabbitMQCommunicationService implements CommunicationService {
    private Connection connection;
    private Map<String, Channel> channels; // Mappa per gestire più canali

    public RabbitMQCommunicationService(String host, String user, String pass) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(user);
        factory.setPassword(pass);
        factory.setVirtualHost("/");
        factory.setPort(5672);

        connection = factory.newConnection();
        channels = new HashMap<>();
    }

    // Metodo per ottenere un canale esistente o crearne uno nuovo per una coda specifica
    private Channel getOrCreateChannel(String queueName) throws Exception {
        if (!channels.containsKey(queueName)) {
            Channel channel = connection.createChannel(); // Crea un canale solo se non esiste già
            channel.queueDeclare(queueName, true, false, false, null); // Dichiarazione della coda come "durable"
            channels.put(queueName, channel);
        }
        return channels.get(queueName);
    }

    @Override
    public void sendMessage(String queueName, String message) throws Exception {
        Channel channel = getOrCreateChannel(queueName); // Ottieni o crea il canale
        channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
    }

    @Override
    public String receiveMessage(String queueName) throws Exception {
        Channel channel = getOrCreateChannel(queueName); // Ottieni o crea il canale
        GetResponse response = channel.basicGet(queueName, true);
        return response != null ? new String(response.getBody()) : null;
    }

    public void startConsuming(String queueName) throws Exception {
        Channel channel = getOrCreateChannel(queueName); // Ottieni o crea il canale
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {

            public void handleDelivery(String consumerTag, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received: " + message);
            }
        });
    }

    public void close() throws Exception {
        // Chiude tutti i canali aperti
        for (Channel channel : channels.values()) {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}
