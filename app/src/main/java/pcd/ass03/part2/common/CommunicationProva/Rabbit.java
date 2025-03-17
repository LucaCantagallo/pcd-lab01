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
        String queueName = gamecode; // Coda con l'ultimo Sudoku

        try {
            channel.queueDeclare(queueName, true, false, false, null);
            GetResponse response = channel.basicGet(queueName, false);

            if (response != null) {
                channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
                String message = new String(response.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Sudoku ricevuto da " + queueName);
                return message;
            }
        } catch (IOException e) {
            System.out.println("Errore nella ricezione del Sudoku: " + e.getMessage());
        }

        return "NON HO RICEVUTO NULLA";
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

        // 🔹 Controlliamo se la pulizia della coda funziona davvero
        try {
            System.out.println("Pulizia della coda prima di inviare il nuovo messaggio...");
            while (channel.basicGet(queueName, true) != null) {
                System.out.println("Messaggio rimosso dalla coda.");
            }
            System.out.println("Coda pulita.");
        } catch (IOException e) {
            System.out.println("Pulizia coda sudoku non riuscita IOException");
        }

        try {
            System.out.println("Invio messaggio aggiornato alla coda principale: " + message);
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Messaggio inviato correttamente: " + message);
        } catch (IOException e) {
            System.out.println("Send canale sudoku non riuscita IOException");
        }
    }




    public void listenForUpdates(String gamecode, Consumer<String> callback) {
        this.reconnect();
        String exchangeName = gamecode + "_exchange"; // Exchange per update in tempo reale

        try {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true);
            String queueName = channel.queueDeclare().getQueue(); // Coda anonima per ogni client
            channel.queueBind(queueName, exchangeName, ""); // Lega la coda all'exchange

            channel.basicConsume(queueName, true, (consumerTag, message) -> {
                String receivedMessage = new String(message.getBody(), StandardCharsets.UTF_8);
                callback.accept(receivedMessage);
            }, consumerTag -> {});

            System.out.println(" [x] In ascolto per aggiornamenti su " + exchangeName);
        } catch (IOException e) {
            System.out.println("Errore nell'ascolto degli aggiornamenti: " + e.getMessage());
        }
    }


    public void updateMessageSudoku(String gamecode, String update) {
        this.reconnect();
        String exchangeName = gamecode + "_exchange"; // Stesso exchange di `listenForUpdates`

        try {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true);
            channel.basicPublish(exchangeName, "", null, update.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Aggiornamento inviato a " + exchangeName);
        } catch (IOException e) {
            System.out.println("Errore nell'invio dell'aggiornamento: " + e.getMessage());
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
