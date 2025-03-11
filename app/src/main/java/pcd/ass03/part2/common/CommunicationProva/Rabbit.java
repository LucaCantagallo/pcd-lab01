package pcd.ass03.part2.common.CommunicationProva;

import com.rabbitmq.client.*;

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

    // ✅ Metodo per ricevere subito il messaggio più recente (se esiste), altrimenti null
    public String receiveMessage(String gamecode) throws IOException {
        String queueName = gamecode;
        channel.queueDeclare(queueName, true, false, false, null); // Assicura che la coda esista

        GetResponse response = channel.basicGet(queueName, true); // Prende il messaggio (se c'è)
        if (response == null) {
            return ""; // Nessun messaggio in coda
        }
        return new String(response.getBody(), StandardCharsets.UTF_8);
    }

    // ✅ Metodo per inviare un messaggio, sovrascrivendo i vecchi nella coda
    public void sendMessage(String gamecode, String message) throws IOException {
        String queueName = gamecode;
        channel.queueDeclare(queueName, true, false, false, null);

        // Pulisce la coda prima di inserire il nuovo messaggio, lasciando sempre solo l'ultimo
        channel.queuePurge(queueName);

        channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent: '" + message + "' to " + queueName);
    }

    public boolean doesQueueExist(String queueName) {
        try {
            // Tentiamo di accedere alla coda passivamente, senza modificarla
            channel.queueDeclarePassive(queueName);
            return true; // La coda esiste
        } catch (IOException e) {
            // Se c'è un'eccezione, significa che la coda non esiste
            return false;
        }
    }

    public void close() throws IOException, TimeoutException {
        if (channel != null) channel.close();
        if (connection != null) connection.close();
    }
}
