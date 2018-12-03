package nordea.project.lru;

/**
 * Contains an entry in the LRU cache
 * 
 * @author choudshe
 *
 */
 class CacheEntry {

	 private long lastAccessTimestamp;
	 private long createdTimestamp;
	 private long countAccess = 1;
	 private Object key;
	 private Object obj;
	 
	 CacheEntry(Object key, Object obj){
		 lastAccessTimestamp = System.currentTimeMillis();
		 createdTimestamp = lastAccessTimestamp;
		 this.key = key;
		 this.obj = obj;
	 }
	
	 private synchronized void accessed() {
		 countAccess++;
		 lastAccessTimestamp = System.currentTimeMillis();
	 }
	 
	 public long getCountAccess() {
		 return countAccess;
	 }
	 
	 public long getCreatedTimestamp() {
		 return createdTimestamp;
	 }
	 
	 public Object getKey() {
		 return key;
	 }
	 
	 public long getLastAccessTimestamp() {
		 return lastAccessTimestamp;
	 }
	 
	 public Object getObject() {
		 accessed();
		 return obj;
	 }
}
