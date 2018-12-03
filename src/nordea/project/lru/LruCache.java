package nordea.project.lru;

import java.util.Hashtable;

/**
 * LRU cache implementation:-
 * 
 * When a cacheEntry is added to cache, a timestamp I set
 * when timestamp times out, a purge thread removes timed out entries.
 * 
 * Purge interval, is how often a "purge" thread will look/do clean up in the cache
 * 
 * Timeout is element time-to-live. If this interval exceeds and object has not been touched
 * element will be removed from cache.
 * 
 * force Timeout is element time-to-live, no questions-asked. Element will be removed when
 * this is exceeded, if value=0, functions is disabled.
 * 
 * @author choudshe
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class LruCache extends Hashtable{

	private static final long serialVersionUID = 1L;

	private static PurgeThread purgeThread = null;
	
	class PurgeThread {
		
	}
}
