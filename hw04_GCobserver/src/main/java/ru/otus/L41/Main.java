package ru.otus.L41;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.MBeanRegistrationException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/*
-Xms512m
-Xmx512m
-Dcom.sun.management.jmxremote.port=15000
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
-verbose:gc
 */

public class Main {

    public static void main(String[] args) throws InterruptedException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, MalformedObjectNameException {

        final Logger logger = LogManager.getLogger(GCobserver.class);

        RuntimeMXBean rmxb = ManagementFactory.getRuntimeMXBean();
        System.out.println("Starting pid: " + rmxb.getName());

        logger.info(rmxb.getName());
        rmxb.getInputArguments()
                .stream()
                .forEach(l->logger.info(l));

        GCobserver.run();

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark mbean = new Benchmark();
        mbs.registerMBean(mbean, name);

        int size = 1024;
        int sleep = 500;
        mbean.setSize(size);
        mbean.setSleep(sleep);
        mbean.run();
    }
}
