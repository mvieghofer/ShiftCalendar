package mvieghofer.github.com.utils;

import java.util.Collections;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

public class SingletonCache {
	private static final SingletonCache instance = new SingletonCache();

	private SingletonCache() {
	}

	public static SingletonCache getInstance() {
		return instance;
	}
	
	private Cache cache;
	
	public Cache getCache() throws CacheException {
		if (cache == null) {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			cache = cacheFactory.createCache(Collections.emptyMap());
		}
		return cache;
	}
}
