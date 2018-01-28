package cache;

/**
 * Created by tully.
 */
public interface CacheEngine<K, V> {

    void put(K key, MyElement<V> element);

    MyElement<V> get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();
}
