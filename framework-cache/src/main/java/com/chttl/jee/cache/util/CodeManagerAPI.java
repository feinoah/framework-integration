package com.chttl.jee.cache.util;

import com.chttl.jee.cache.MemoryCache;

public interface CodeManagerAPI {

	//key: startWith gettable#entityName 
	public <T> void reloadEntity(Class<T> entityClass) ;
	
	//key: startWith getcodetable#entityName
	public <T> void reloadCodeEntity(Class<T> entityClass) ;
	
	public MemoryCache getMemoryCache(String cacheName) ;
	
}