package lruCache;

public class TestLRUCache {

	public static void main(String[] args) {
		LRUCache cache = new LRUCache(5);
		
		cache.set(4, 1234);
		cache.set(5, 1235);
		cache.set(6, 1236);
		cache.set(7, 1237);
		cache.set(8, 1238);
		cache.set(9, 1239);
		
		cache.get(6);
		
	}
}
