package cache;

import myorm.MyORMConfig;
import myorm.MyORMparam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

/**
 * Created by tully.
 */
public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    Logger log = LoggerFactory.getLogger("Common");
    private static final int TIME_THRESHOLD_MS = 5;

    private MyORMConfig myORMConfig;

    private int maxElements;
    private long lifeTimeMs;
    private long idleTimeMs;
    private boolean isEternal;

    private Map<K, SoftReference<MyElement<V>>> elements;
    private Timer timer;

    private int hit = 0;
    private int miss = 0;

    public CacheEngineImpl(MyORMConfig myORMConfig) {
        this.myORMConfig = myORMConfig;
        this.elements = new LinkedHashMap<>();
        this.maxElements = Integer.parseInt(myORMConfig.getParameter(MyORMparam.CACHE_MAX_ELEMENTS));
        this.lifeTimeMs = Long.parseLong(myORMConfig.getParameter(MyORMparam.CACHE_LIFE_TIME_MS));
        this.idleTimeMs = Long.parseLong(myORMConfig.getParameter(MyORMparam.CACHE_IDLE_TIME_MS));
        this.isEternal = Boolean.parseBoolean(myORMConfig.getParameter(MyORMparam.CACHE_IS_ETERNAL));
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
        this.timer = new Timer();
    }

    public void put(K key, MyElement<V> element) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        elements.put(key, new SoftReference(element));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    public MyElement<V> get(K key) {
        MyElement<V> element = null;
        SoftReference<MyElement<V>> reference = elements.get(key);
        if (reference != null) {
            element = elements.get(key).get();
            if (element != null) {
                hit++;
                element.setAccessed();
            }
        } else {
            miss++;
        }
        return element;
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final K key, Function<MyElement<V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                MyElement<V> element = null;
                SoftReference<MyElement<V>> reference = elements.get(key);
                if (reference != null) {
                    element = elements.get(key).get();
                    if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                        elements.remove(key);
                        this.cancel();
                    }
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
