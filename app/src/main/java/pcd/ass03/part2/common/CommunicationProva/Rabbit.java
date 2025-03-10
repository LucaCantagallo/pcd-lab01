package pcd.ass03.part2.common.CommunicationProva;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
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

        /*    channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String message = String.join(" ", argv);
            channel.basicPublish("", "hello", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");*/

    public void generateSudokuQueue(String gamecode) throws IOException {
        channel.queueDeclare(gamecode, true, false, false, null);
    }

    public void addPlayerAsRecv(String gamecode) throws IOException {
        channel.queueDeclare(gamecode, true, false, false, null);
        System.out.println(" [*] New player for "+ gamecode);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");


        };
        channel.basicConsume(gamecode, true, deliverCallback, consumerTag -> { });

        System.out.println(" [*] Done with setup.");
    }

}
