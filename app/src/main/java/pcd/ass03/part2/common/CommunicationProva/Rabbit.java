package pcd.ass03.part2.common.CommunicationProva;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Rabbit {

    private Connection connection;
    private Channel channel;
    public Rabbit() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
    }

    public void receiveMessage(String gamecode) throws IOException {
        String queueName = gamecode;
        channel.queueDeclare(queueName, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received: " + message);
            //qui si crea griglia e si mette nel db?
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        System.out.println(" [*] Listening on queue: " + queueName);
    }

    public void sendMessage(String gamecode, String message) throws IOException {
        String queueName = gamecode;

        // Assicuriamoci che la coda esista prima di inviare il messaggio
        channel.queueDeclare(queueName, true, false, false, null);

        // Pubblica il messaggio sulla coda
        channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent: '" + message + "' to " + queueName);
    }

    public void close() throws IOException, TimeoutException {
        if (channel != null) {
            channel.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

}
