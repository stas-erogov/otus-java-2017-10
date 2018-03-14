package runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessStreamHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ProcessStreamHandler.class);

    private final InputStream inputStream;
    private final Process process;

    public ProcessStreamHandler(InputStream inputStream, Process process) {
        this.inputStream = inputStream;
        this.process = process;
    }
    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        try (InputStreamReader isr = new InputStreamReader(inputStream)) {
            bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logger.info(line);
            }
        } catch (IOException e) {
            logger.error("Process Handler Error", e);
        }
    }
}