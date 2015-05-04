package com.chttl.jee.cache;

import java.util.List;

public interface MemoryCache {

	public void put(Object key, Object value);
	
	public boolean putIfAbsent(Object key, Object value);
	
	public Object putIfAbsentWithValue(Object key, Object value);
	
	public Object get(Object key) ;
	
	public List<Object> getKeys() ;
	
	public boolean remove(Object key) ;
	
	public void removeAll();
	
	public Object getDelegate() ; //取得實際上運作的快取物件
	
}
