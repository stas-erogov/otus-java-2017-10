package cache;

/**
 * Created by tully.
 */
@SuppressWarnings("WeakerAccess")
public class MyElement<V> {
    private final V value;
    private final long creationTime;
    private long lastAccessTime;


    public MyElement(V value) {
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public V getValue() {
        return value;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }
}
