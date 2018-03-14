package core;

import msgserver.MessageSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import runner.ProcessStarter;
import server.MessageSocketServer;

import java.util.Map;

@Configuration
@PropertySource("classpath:services.properties")
public class MessageServerConfig {

    @Bean(name = "messageSystem")
    public MessageSystem getMessageSystem() {
        return new MessageSystem();
    }

    @Bean(name = "messageSocketServer")
    public MessageSocketServer getMessageSocketServer() {
        return new MessageSocketServer(getMessageSystem());
    }

    @Value("#{${services}}")
    private Map<String, String> services;

    @Value("#{${delay}}")
    private long delay;

    @Bean(name = "processStarter")
    public ProcessStarter getProcessStarter() {
        return new ProcessStarter(services, delay);
    }

}
