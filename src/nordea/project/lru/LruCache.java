package nordea.project.lru;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
	private static Vector purgeList = new Vector();
	static long purgeInterval = 10*1000; // sleep between cache checks, default 10 sec
	private long cacheSize = 1000; // max elements in cache
	private long timeout = 5*60*1000; // element time-to-live, not touched, defauult 5 mins
	private long forceTimeout = 30*60*1000; // element time-to-live, no condition, default 30 mins
	private LruCacheObjectGetter fetcher;
	private String name;
	private Vector listeners = new Vector();
	transient private static Logger cat = LoggerFactory.getLogger(LruCache.class);
	
	static class PurgeThread implements Runnable{

		private boolean doRun = true;
		
		public void pleaseStop() {
			doRun = false;
		}
		
		@Override
		public void run() {
			while(doRun) {
				try {
					//do cleanup stuff
					Thread.sleep(purgeInterval);
					
					if(!doRun)
						continue;
					
					//Now, purge all instance
					Vector list =(Vector)purgeList.clone();
					
					Thread.currentThread().setName("LRU cache purge thread - " + list.size() + "live LRU caches.");
					
					for(int i =0;i< list.size();i++) {
						LruCache cache = (LruCache) list.get(i);
						try {
							cache.purge();
						}catch (Throwable e) {
							cat.error("Problem cleaning up LruCache, for"+ cache.name,e);
						}
					}
				}catch(Throwable e) {
					cat.error("Problem cleaning up LruCache, for",e);
				}
			}
			
		}
		
		
	}
	
	public LruCache() {
		synchronized (LruCache.class) {
			purgeList.add(this);
			init();	
		}
	}
	
	public LruCache(LruCacheObjectGetter fetcher, String name) {
		this.fetcher = fetcher;
		this.name = name;
		
		synchronized (LruCache.class) {
			purgeList.add(this);
			init();
		}
	}
	
	public void addListener(CacheListener listener) {
		if(listener != null && !listeners.contains(listener))
			listeners.addElement(listener);
	}
	
	public void removeListener(CacheListener listener) {
		if(listener != null && !listeners.contains(listener))
			listeners.remove(listener);
	}
	
	/**
	 * Flush cache
	 */
	public synchronized void flush() {
		notifyFlush(super.elements());
		super.clear();
	}
	
	/**
	 * getObject. First check cache to see in object if already there
	 * if not, get object usinng fetcher and store in cache.
	 * @return java.lang.Object
	 * @param key java.lang.Object
	 */
	public Object get(Object key) {
		CacheEntry ce;
		ce = (CacheEntry) super.get(key);
		if(ce ==null && fetcher == null) {
			notifyMiss(key);
			return null;
			
		}
		
		if(ce ==null && fetcher != null) {
			if((ce =(CacheEntry) super.get(key)) == null) {
				notifyMiss(key);
				//object is not in cache, get using fetcher
			}
			// The call to getObject is not synchronized, but since it might
			//(usually will) go through the network, it is a bad
			// idea to sync on it for a long time.
			Object obj = fetcher.getObject(key);
			if(obj != null) 
				store(key,obj);
			return obj;
		}else {
			notifyHit(key);
		}
		return ce.getObject();
	}
	
	private void notifyMiss(Object key) {
		for(int i =0;i < listeners.size();i++) {
			CacheListener listener = (CacheListener) listeners.elementAt(i);
			listener.miss(key);
		}
	}
	
	private void notifyAdded(Object key, Object object) {
		for(int i =0;i < listeners.size();i++) {
			CacheListener listener = (CacheListener) listeners.elementAt(i);
			listener.added(key, object);
		}
	}
	
	private void notifyRemoved (Object key, Object object, int reason) {
		for(int i =0;i < listeners.size();i++) {
			CacheListener listener = (CacheListener) listeners.elementAt(i);
			listener.removed(key, object, reason);
		}
	}
	
	private void notifyHit(Object key) {
		for(int i =0;i < listeners.size();i++) {
			CacheListener listener = (CacheListener) listeners.elementAt(i);
		listener.hit(key);	
		}
	}
	
	private void notifyFlush(Enumeration e) {
		for(int i =0;i < listeners.size();i++) {
			CacheListener listener = (CacheListener) listeners.elementAt(i);
		listener.flushed(e);	
		}
	}
	
	public long getCacheSize() {
		return cacheSize;
	}
	
	public synchronized long getForceTimeout() {
		return forceTimeout;
	}
	
	public synchronized long getPurgeInterval() {
		return purgeInterval;
	}
	
	public synchronized long getTimeout() {
		return timeout;
	}
	
	public synchronized Object[] getAllObjects() {
		Vector v = new Vector();
		Enumeration en = super.elements();
		while(en.hasMoreElements()) {
			CacheEntry entry = (CacheEntry) en.nextElement();
			
			v.addElement(entry.getObject());
		}
		Object[] oa = new Object[v.size()];
		v.copyInto(oa);
		return oa;
	}
	
	/**
	 * init
	 */
	private synchronized static void init() {
		if(purgeThread == null) {
			purgeThread = new PurgeThread();
			// start cleanup thread
			Thread t = new Thread(purgeThread);
			t.setPriority(1);
			t.setDaemon(true);
			t.setName("LruCache flusher");
			t.start();
		}
	}
	
	
	public synchronized Object put(Object key, Object obj) {
		Object ret = store(key, obj);
		return ret;
	}
	
	/**
	 * Remove timed out elements from cache.
	 */
	
	private synchronized void removeAccessedOnlyOnce() {
		for(java.util.Enumeration en = elements(); en.hasMoreElements();) {
			//look through cache, find timed out entries and remove em
			CacheEntry ce = (CacheEntry) en.nextElement();
			if(ce.getCountAccess() <= 2) {
				Object key = ce.getKey();
				Object object = ce.getObject();
				super.remove(key);
				notifyRemoved(key,object,CacheListener.REASON_REMOVED_ONLY_ONCE);
			}
		}
	}
	
	/**
	 * Remove timed out elements from cache.
	 */
	
	private synchronized void removeForceTimedOut() {
		//forceTimeout == 0, ignore...
		if(getForceTimeout()<= 0) {
			return;
		}
		long currentTime = System.currentTimeMillis();
		for(java.util.Enumeration en = elements(); en.hasMoreElements();) {
			//look through cache, find timed out entries and remove em
			
			CacheEntry ce = (CacheEntry) en.nextElement();
			if(currentTime - ce.getCreatedTimestamp() > getForceTimeout()) {
				Object key = ce.getKey();
				Object object = ce.getObject();
				super.remove(key);
				notifyRemoved(key, object, CacheListener.REASON_FORCE_TIMEOUT);
			}
		}
	}
	
	/**
	 * Remove timed out elements from cache.
	 */
	private synchronized void removeTimedOut() {
		long currentTime = System.currentTimeMillis();
		if(getTimeout()<=0) {
			return;
		}
		for(java.util.Enumeration en = elements(); en.hasMoreElements();) {
			//look through cache, find timed out entries and remove em
		
			CacheEntry ce = (CacheEntry) en.nextElement();
			if(currentTime - ce.getLastAccessTimestamp() > getTimeout()) {
				Object key = ce.getKey();
				Object object = ce.getObject();
				super.remove(key);
				notifyRemoved(key, object, CacheListener.REASON_TIMEOUT);
			}
		}
		
	}
	
	private void purge() {
		if(size()>getCacheSize()) {
			// force clean of oldest
			removeForceTimedOut();
			removeTimedOut();
			if(size()>getCacheSize()) {
				//if still too large, force clean of elements accessed only once
				removeAccessedOnlyOnce();
			}
		}else {
			// just do normal cleanup, remove timed out entries
			removeForceTimedOut();
			removeTimedOut();
		}
	}
	
	
	public void setCacheSize(long newCacheSize) {
		cacheSize = newCacheSize;
	}
	
	public synchronized void setTimeout(long newTimeout) {
		timeout = newTimeout;
	}
	
	public synchronized void setForceTimeout(long newTimeout) {
		forceTimeout = newTimeout;
	}
	
	
	private Object store(Object key, Object obj) {
		notifyAdded(key,obj);
		CacheEntry ce = new CacheEntry(key,obj);
		return super.put(key, ce);
	}
	
	public synchronized Object remove(Object key) {
		Object obj = super.get(key);
		
		if(obj!= null) {
			CacheEntry ce = (CacheEntry) obj;
			notifyRemoved(ce.getKey(), ce.getObject(), CacheListener.REASON_MANUAL_REMOVE);
		}
		
		return super.remove(key);
	}
	
	
	public void destory() {
		synchronized(LruCache.class) {
			purgeList.remove(this);
			
			if(purgeList.size() ==0 && purgeThread != null) {
				purgeThread.pleaseStop();
				purgeThread = null;
			}
		}
	}
	
	public static void setPurgeInterval(long newPurgeInterval) {
		purgeInterval = newPurgeInterval;
	}
}
