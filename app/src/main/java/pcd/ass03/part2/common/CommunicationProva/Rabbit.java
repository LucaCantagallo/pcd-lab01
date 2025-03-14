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

    public Rabbit() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();

        channel.queueDeclare(this.GLOBALGAMECODENAME, true, false, false, null); // Assicura che la coda esista

    }

    public String receiveGlobalGameCodes() throws IOException {


        GetResponse response = channel.basicGet(this.GLOBALGAMECODENAME, true); // Prende il messaggio (se c'è)
        if (response == null) {
            return ""; // Nessun messaggio in coda
        }
        return new String(response.getBody(), StandardCharsets.UTF_8);
    }

    public void sendGlobalGameCodes(String message) throws IOException {

        // Pulisce la coda prima di inserire il nuovo messaggio, lasciando sempre solo l'ultimo
        channel.queuePurge(this.GLOBALGAMECODENAME);

        channel.basicPublish("", this.GLOBALGAMECODENAME, null, message.getBytes(StandardCharsets.UTF_8));
        //System.out.println(" [x] Sent: '" + message + "' to " + queueName);
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

        channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
        //System.out.println(" [x] Sent: '" + message + "' to " + queueName);
    }

    public void listenForUpdates(String gamecode, Consumer<String> callback){
        String queueName = gamecode+"_updates";
        try {
            System.out.println("Sono in ascolto Dani");
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicConsume(queueName, true, (consumerTag, message) -> {
                String receivedMessage = new String(message.getBody(), StandardCharsets.UTF_8);
                callback.accept(receivedMessage);
            }, consumerTag -> {});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMessageSudoku(String gamecode, String message) {
        String queueName = gamecode + "_updates";
        try {
            channel.queueDeclare(queueName, true, false, false, null);

            // Invia il messaggio SENZA cancellare la coda (per tenere traccia degli aggiornamenti)
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("🔄 Aggiornamento inviato a " + queueName);
        } catch (IOException e) {
            throw new RuntimeException("Errore nell'aggiornamento della coda " + queueName, e);
        }
    }




    public void close() throws IOException, TimeoutException {
        if (channel != null) channel.close();
        if (connection != null) connection.close();
    }
}
