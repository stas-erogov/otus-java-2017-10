package ru.otus.L41;

import com.sun.management.GarbageCollectionNotificationInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;

public class GCobserver {

    private static final Logger logger = LogManager.getLogger(GCobserver.class);

    private static final Set<String> YOUNG = new HashSet<>();
    private static final Set<String> OLD = new HashSet<>();

    static {
        YOUNG.add("PS Scavenge");
        YOUNG.add("ParNew");
        YOUNG.add("G1 Young Generation");
        YOUNG.add("Copy");

        OLD.add("PS MarkSweep");
        OLD.add("MarkSweepCompact");
        OLD.add("ConcurrentMarkSweep");
        OLD.add("G1 Mixed Generation");
    }

    private static GCData young = new GCData("YOUNG");
    private static GCData old = new GCData("OLD");

    public static void run() {
        List<GarbageCollectorMXBean> gcmxbs = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gcbean : gcmxbs) {
            NotificationListener notificationListener = (notification, o) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    CompositeData cd = (CompositeData) notification.getUserData();
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from(cd);

                    if (YOUNG.contains(info.getGcName())) {
                        young.addStat(info.getGcInfo().getDuration());
                    } else if (OLD.contains(info.getGcName())) {
                        old.addStat(info.getGcInfo().getDuration());
                    }
                }
            };

            NotificationEmitter notificationEmitter = (NotificationEmitter) gcbean;
            notificationEmitter.addNotificationListener(notificationListener, null, null);
        }

        Thread t = new PrintLog();
        t.start();
    }

    private static class PrintLog extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    logger.info(young);
                    logger.info(old);
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
