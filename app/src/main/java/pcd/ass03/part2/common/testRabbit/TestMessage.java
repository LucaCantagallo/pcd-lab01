package pcd.ass03.part2.common.testRabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestMessage {

    private Channel channel;

    private static final String EXCHANGE_CREATE = "create";
    private static final String EXCHANGE_UPDATE = "update";
    private static final String EXCHANGE_SELECT = "select";
    private static final String EXCHANGE_UNSELECT = "unselect";
    private static final String EXCHANGE_JOIN = "join";

    public TestMessage() throws IOException, TimeoutException {
        this.setupConnection();

        this.channel.exchangeDeclare(EXCHANGE_CREATE, "fanout");
        this.channel.exchangeDeclare(EXCHANGE_UPDATE, "fanout");
        this.channel.exchangeDeclare(EXCHANGE_SELECT, "fanout");
        this.channel.exchangeDeclare(EXCHANGE_UNSELECT, "fanout");
        this.channel.exchangeDeclare(EXCHANGE_JOIN, "fanout");

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

        String submitQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(submitQueueName, EXCHANGE_JOIN, "");
        channel.basicConsume(submitQueueName, true, joinGridCallBack(), t -> {});
    }

    private DeliverCallback joinGridCallBack() {
        return null;
    }

    private DeliverCallback unselectCellCallBack() {
        return null;
    }

    private DeliverCallback selectCellCallBack() {
        return null;
    }

    private DeliverCallback updateGridCallBack() {
        return null;
    }

    private DeliverCallback addGridCallBack() {
        return null;
    }

    void setupConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
    }
}
