package msgserver;

import java.util.Map;

public class MessageSystemContext {
    private MessageSystem messageSystem;

    private Map<String, Object> ctxRegistry;

    private Map<String, Class<?>> msgMapping;

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public Map<String, Object> getCtxRegistry() {
        return ctxRegistry;
    }

    public void setCtxRegistry(Map<String, Object> ctxRegistry) {
        this.ctxRegistry = ctxRegistry;
    }

    public Map<String, Class<?>> getMsgMapping() {
        return msgMapping;
    }

    public void setMsgMapping(Map<String, Class<?>> msgMapping) {
        this.msgMapping = msgMapping;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public void init() {
        for (Map.Entry<String, Object> entry : ctxRegistry.entrySet()) {
            putContextSubscriber(entry.getKey(), entry.getValue());
        }
    }

    public void putContextSubscriber(String name, Object subscriber) {
        ctxRegistry.put(name, subscriber);
        messageSystem.addSubscriber(name);
    }

    public Object getSubscriber(String name) {
        return ctxRegistry.get(name);
    }
}
