package main;

import dbservice.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        logger.info("Context created");
        DBService dbService = (DBService) context.getBean("dbService");
        logger.info(dbService.getLocalStatus());
    }
}
