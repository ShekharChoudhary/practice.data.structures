package nordea.project.lru;

import java.util.Enumeration;

public interface CacheListener {

	public static final int REASON_TIMEOUT = 1;
	public static final int REASON_FORCE_TIMEOUT = 2;
	public static final int REASON_REMOVED_ONLY_ONCE = 3;
	public static final int REASON_MANUAL_REMOVE = 4;
	
	public void hit(Object key);
	public void miss(Object key);
	public void removed(Object key, Object object, int reason);
	public void added(Object key, Object object);
	@SuppressWarnings("rawtypes")
	public void flushed( Enumeration values);
}
