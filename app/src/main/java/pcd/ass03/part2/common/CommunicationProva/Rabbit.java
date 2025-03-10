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

    public void setSudokuQueue(String gamecode) throws IOException {
        String queueName = "send_"+gamecode;
        channel.queueDeclare(queueName, true, false, false, null);


    }

    public void getSudokuQueue(String gamecode) throws IOException {
        String queueName = "receive_"+gamecode;
        channel.queueDeclare(queueName, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");


        };
        channel.basicConsume(gamecode, true, deliverCallback, consumerTag -> { });

        System.out.println(" [*] Done with setup.");
    }

    public void setSudoku(){

    }

}
