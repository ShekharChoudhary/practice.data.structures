package hash.map;

public class MyNewHM {

	private static final int SIZE = 16;
	
	private Entry table[] = new Entry[SIZE];
	
	class Entry{
		
		final String key;
		String value;
		Entry next;
		
		Entry(String k, String v) {
			this.key = k;
			this.value = v;
		}
		
		public void setValue(String value){
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
		
		
	}
	
	
	public void put(String k, String v) {
		
		int hash = k.hashCode()%16;
		Entry e = table[hash];
		if(e!=null) {
			if(e.key.equals(k)) {
				e.value = v;
			}else {
				
				while(e.next != null) {
					e = e.next;
					if(e.key.equals(k)) {
						e.value = v;
						return;
					}
				}
				Entry inputInOldBucket = new Entry(k, v);
				e.next = inputInOldBucket;
			}
			
		}else {
			Entry newInput = new Entry(k,v);
			table[hash] = newInput;
		}
		
	}
	
	public String get(String k) {
		int hash = k.hashCode()%16;
		Entry e = table[hash];
		
		if(e != null) {
			if(e.key.equals(k)) {
				return e.value;
			}else {
				while(e.next != null) {
					e = e.next;
					if(e.key.equals(k)) {
						return e.value;
					}
				}
			}
		}else {
			return null;
		}
		
		
		return null;
	}
	
	public static void main(String[] args) {
		MyNewHM map = new MyNewHM();
		map.put("one", "one");
		map.put("two", "two");
		map.put("three", "first three");
		map.put("three", "three");
		map.put("four", "four");
		
		System.out.println(map.get("one"));
		System.out.println(map.get("two"));
		System.out.println(map.get("three"));
		System.out.println(map.get("four"));
	}
	
}
