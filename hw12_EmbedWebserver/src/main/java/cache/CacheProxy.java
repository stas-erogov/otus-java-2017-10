package cache;

public class CacheProxy implements CacheService {
    private CacheEngine cacheEngine;

    public CacheProxy(CacheEngine cacheEngine) {
        this.cacheEngine = cacheEngine;
    }

    @Override
    public int getHitCount() {
        return cacheEngine.getHitCount();
    }

    @Override
    public int getMissCount() {
        return cacheEngine.getMissCount();
    }
}
