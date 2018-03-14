package msgserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.MessageWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageSystem implements EndPointService {
    private final static Logger logger = LoggerFactory.getLogger(MessageSystem.class);
    private final Map<String, ConcurrentLinkedQueue<Message>> registry = new HashMap<>();
    private final List<Thread> pool = new ArrayList<>();
    private final Map<String, MessageWorker> workers = new ConcurrentHashMap<>(new HashMap<>());

    public MessageSystem() {
    }

    public void addSubscriber(String subscriber, MessageWorker worker) {
        registry.put(subscriber, new ConcurrentLinkedQueue<>());
        workers.put(subscriber, worker);
        startSubsMsgThread(subscriber);
    }

    public void removeSubscriber(String subscriber) {
        registry.remove(subscriber);
    }

    public void sendMessage(Message message) {
        String receiver = message.getReceiver();
        logger.info("send Message: [message:]" + message.toString() + "[receiver:]" + receiver);
        registry.get(receiver).add(message);
        for (Map.Entry<String, ConcurrentLinkedQueue<Message>> entry : registry.entrySet()) {
            logger.info("    " + entry.getKey() + ":" + entry.getValue());
        }
    }

    private void startSubsMsgThread (String subscriber) {
        Thread thread = new Thread(()->{
            while (true) {
                ConcurrentLinkedQueue<Message> queue = registry.get(subscriber);
                while(!queue.isEmpty()) {
                    Message message = queue.poll();
                    workers.get(subscriber).accept(message);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.info("Thread interrupted, finishing: " + subscriber);
                    return;
                }
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("Finishing: " + subscriber);
                    return;
                }
            }
        });
        thread.setName(this.getClass().getSimpleName() + "_" +subscriber);
        logger.info("Thread created: " + thread.getName());
        thread.start();
        logger.info("Thread started: "+ thread.getName() + ", state: " + thread.getState());
        pool.add(thread);
    }

    public void stop() {
        pool.forEach(Thread::interrupt);
    }

    @Override
    public void execute(Message message) {
        sendMessage(message);
    }
}
