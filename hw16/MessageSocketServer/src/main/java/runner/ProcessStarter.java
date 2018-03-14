package runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProcessStarter {

    private static final Logger logger = LoggerFactory.getLogger(ProcessStarter.class);

//    private static final String CMD_LINE_1 ="java -Ddbname=dbsource1 -Ddb.url=jdbc:h2:file:./dbfile1 -jar DBService-1.0-SNAPSHOT.jar";
//    private static final String WORKING_DIR_1 ="./dbService1";
//    private static final String CMD_LINE_2 ="java -Ddbname=dbsource2 -Ddb.url=jdbc:h2:file:./dbfile2 -jar DBService-1.0-SNAPSHOT.jar";
//    private static final String WORKING_DIR_2 ="./dbService2";
//    private static final String CMD_LINE_3 ="java -jar FrontendService-1.0-SNAPSHOT-jar-with-dependencies.jar 8051";
//    private static final String WORKING_DIR_3 ="./Frontend1";
//    private static final String CMD_LINE_4 ="java -jar FrontendService-1.0-SNAPSHOT-jar-with-dependencies.jar 8052";
//    private static final String WORKING_DIR_4 ="./Frontend2";

    private long delay;
    private Map<String, String> services;

    public ProcessStarter(Map<String, String> services, long delay) {
        this.services = services;
        this.delay = delay;
    }

    @PostConstruct
    public void init() {

//        services.put(WORKING_DIR_1, CMD_LINE_1);
//        services.put(WORKING_DIR_2, CMD_LINE_2);
//        services.put(WORKING_DIR_3, CMD_LINE_3);
//        services.put(WORKING_DIR_4, CMD_LINE_4);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        for (Map.Entry<String, String> service : services.entrySet()) {
            executorService.schedule(() -> {
                logger.info("Start scheduled process: " + service.getValue());
                ProcessBuilder pb = new ProcessBuilder(service.getValue().split(" "));
                pb.directory(new File(service.getKey()));
                pb.redirectErrorStream(true);
                Process p = null;
                try {
                    p = pb.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream inputStream = p.getInputStream();
                ProcessStreamHandler handler = new ProcessStreamHandler(inputStream, p);
                handler.start();
            }, delay, TimeUnit.SECONDS);
            delay += delay;
        }
    }
}
