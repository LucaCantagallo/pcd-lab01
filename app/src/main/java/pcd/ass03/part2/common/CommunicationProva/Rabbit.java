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
        //System.out.println("1");
        String queueName = gamecode;
        //System.out.println("2");
        channel.queueDeclare(queueName, false, false, false, null); // Assicura che la coda esista
        //System.out.println("3");
        GetResponse response = channel.basicGet(queueName, true); // Prende il messaggio (se c'è)
        //System.out.println("4");
        if (response == null) {
            //System.out.println("5a");
            return "NON HO RICEVUTO NULLA"; // Nessun messaggio in coda
        } else {
            //System.out.println("5");
            String message = new String(response.getBody(), StandardCharsets.UTF_8);
            //System.out.println("6");
            System.out.println(message + "inviato a " + queueName);
            //System.out.println("7");
            return new String(response.getBody(), StandardCharsets.UTF_8);
        }
    }

    // ✅ Metodo per inviare un messaggio, sovrascrivendo i vecchi nella coda
    public void sendMessage(String gamecode, String message) throws IOException {
        System.out.println("a");
        String queueName = gamecode;
        channel.queueDeclare(queueName, false, false, false, null);

        // Pulisce la coda prima di inserire il nuovo messaggio, lasciando sempre solo l'ultimo
        channel.queuePurge(queueName);

        channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent: '" + message + "' to " + queueName);
    }


    public void listenForUpdates(String gamecode, Consumer<String> callback){
        String queueName = gamecode;
        try {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicConsume(queueName, true, (consumerTag, message) -> {
                this.receiveMessage(gamecode);
                String receivedMessage = new String(message.getBody(), StandardCharsets.UTF_8);
                callback.accept(receivedMessage); //Essenziale, non cancellare
            }, consumerTag -> {});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMessageSudoku(String gamecode) {
        String queueName = gamecode + "_updates";
        try {
            channel.queueDeclare(queueName, true, false, false, null);

            // Invia il messaggio SENZA cancellare la coda (per tenere traccia degli aggiornamenti)
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, "notification".getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Errore nell'aggiornamento della coda " + queueName, e);
        }
    }




    public void close() throws IOException, TimeoutException {
        if (channel != null) channel.close();
        if (connection != null) connection.close();
    }
}
