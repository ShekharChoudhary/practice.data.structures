package nordea.project.lru;

/**
 * This interface must be implemented by the user of the LRU cache - it is used to get an entry 
 * which will then be added to the cache.
 * 
 * @author choudshe
 *
 */
public interface LruCacheObjectGetter {

	/**
	 * Get the desired object, with the given key from the backing store- if null is returned, it is assumed that
	 * the key was not found
	 */
	
	Object getObject(Object key);
}
