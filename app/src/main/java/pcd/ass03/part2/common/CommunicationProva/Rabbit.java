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

    public Rabbit(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            this.connection = factory.newConnection();
        } catch (IOException e) {
            System.out.println("Connessione non riuscita IOException");
        } catch (TimeoutException e) {
            System.out.println("Connessione non riuscita TimeOutException");
        }
        try {
            this.channel = connection.createChannel();
        } catch (IOException e) {
            System.out.println("Canale non creato RunTimeException");
        }

        try {
            channel.queueDeclare(this.GLOBALGAMECODENAME, true, false, false, null); // Assicura che la coda esista
        } catch (IOException e) {
            System.out.println("Creazione coda globali non riuscita IOException");
        }

    }

    public String receiveGlobalGameCodes() {
        this.reconnect();

        GetResponse response = null; // Prende il messaggio (se c'è)
        try {
            response = channel.basicGet(this.GLOBALGAMECODENAME, true);
        } catch (IOException e) {
            System.out.println("Get code globali non riuscita IOException");
        }
        if (response == null) {
            return ""; // Nessun messaggio in coda
        }
        return new String(response.getBody(), StandardCharsets.UTF_8);
    }

    public void sendGlobalGameCodes(String message) {
        this.reconnect();
        try {
            channel.queuePurge(this.GLOBALGAMECODENAME);
        } catch (IOException e) {
            System.out.println("Pulizia coda globale non riuscita IOException");
        }

        try {
            channel.basicPublish("", this.GLOBALGAMECODENAME, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Invio messaggio globali non riuscita IOException");
        }
        //System.out.println(" [x] Sent: '" + message + "' to " + queueName);
    }

    // ✅ Metodo per ricevere subito il messaggio più recente (se esiste), altrimenti null
    public String receiveMessage(String gamecode) {
        this.reconnect();
        //System.out.println("1");
        String queueName = gamecode;
        //System.out.println("2");
        try {
            channel.queueDeclare(queueName, true, false, false, null); // Assicura che la coda esista
        } catch (IOException e) {
            System.out.println("DeclareReceive coda sudoku non riuscita IOException");
        }
        //System.out.println("3");
        GetResponse response = null; // Prende il messaggio (se c'è)
        try {
            response = channel.basicGet(queueName, true);
        } catch (IOException e) {
            System.out.println("Risposta canale sudoku non ottenuta correttamente IOException");
        }
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
    public void sendMessage(String gamecode, String message) {
        this.reconnect();
        String queueName = gamecode;
        try {
            channel.queueDeclare(queueName, true, false, false, null);
        } catch (IOException e) {
            System.out.println("DeclareSend coda sudoku non riuscita IOException");
        }

        // Pulisce la coda prima di inserire il nuovo messaggio, lasciando sempre solo l'ultimo
        try {
            channel.queuePurge(queueName);
        } catch (IOException e) {
            System.out.println("Pulizia coda sudoku non riuscita IOException");
        }

        try {
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Send canale sudoku non riuscita IOException");
        }
        System.out.println(" [x] Sent: '" + message + "' to " + queueName);
    }


    public void listenForUpdates(String gamecode, Consumer<String> callback){
        this.reconnect();
        String queueName = gamecode;
        try {
            channel.queueDeclare(queueName, true, false, false, null);
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
        this.reconnect();
        String queueName = gamecode + "_updates";
        try {
            channel.queueDeclare(queueName, true, false, false, null);

            // Invia il messaggio SENZA cancellare la coda (per tenere traccia degli aggiornamenti)
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, "notification".getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Errore nell'aggiornamento della coda " + queueName, e);
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
                System.out.println("[Rabbit] Connessione e canale ristabiliti.");
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
