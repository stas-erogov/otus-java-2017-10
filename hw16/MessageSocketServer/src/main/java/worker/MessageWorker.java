package worker;

import com.google.gson.Gson;
import messages.HandshakeMessage;
import msgserver.EndPointService;
import msgserver.Message;
import msgserver.MessageSystem;
import msgserver.MessageUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageWorker {
    private static final Logger logger = LoggerFactory.getLogger(MessageWorker.class);
    private static final int WORKERS_COUNT = 2;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;

    private String subscriber;
    private EndPointService endPointService;
    private MessageSystem messageSystem;

    public MessageWorker(Socket socket) {
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        this.socket = socket;
        logger.info("Message worker created: " + this.toString() + " on socket: " + socket.toString());
    }

    public void setSubscriber(String subscriber) {
        logger.info("Set subscriber " + subscriber + " on worker: " + this.toString());
        this.subscriber = subscriber;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public EndPointService getEndPointService() {
        return endPointService;
    }

    public void setEndPointService(EndPointService endPointService) {
        this.endPointService = endPointService;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public void setMessageSystem(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public void accept(Message message) {
        output.add(message);
        logger.info("Add message " + message.toString() + " in queue: " + output);
    }

    public Message pool() {
        return input.poll();
    }

    public Message take() throws InterruptedException {
        return input.take();
    }

    public void close() throws IOException {
        executor.shutdown();
    }

    public void init() {
        if (subscriber == null) {
            accept(new HandshakeMessage(null, ""));
        }
        logger.info("Message worker init fired");
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            logger.info("Writer to socket found: " + out.toString());
            while (socket.isConnected()) {
                logger.info("Socket " + socket.toString() + " is connected");
                Message message = output.take(); //blocks
                logger.info("Message " + message.toString() + " taked from " + output);
                String json = new Gson().toJson(message);
                logger.info("Jsoning message: " + json);
                out.println(json);
                out.println();//line with json + an empty line
                logger.info("Message printed away");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            logger.info("Reader from socket found: " + in.toString());
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) { //blocks
                logger.info("Message received: " + inputLine);
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty line is the end of the message
                    String json = stringBuilder.toString();
                    logger.info("Message in json loaded: " + json);
                    Message message = MessageUtils.getMessageFromJSON(json);
                    logger.info("Message deserzed: " + message.toString());

                    if (message instanceof HandshakeMessage && message.getSender() != null) {
                        messageSystem.addSubscriber(message.getSender(), this);
                    } else {
                        endPointService.execute(message);
                    }
                    logger.info("Message executed by service: " + endPointService);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException | ParseException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
