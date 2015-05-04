package com.chttl.jee.cache;

import java.util.List;

import lombok.Getter;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class MemoryCacheImpl implements MemoryCache{

	private CacheManager manager ;
	@Getter private String cacheName ;
	
	public MemoryCacheImpl(CacheManager mgr,String cacheName){
		this.manager = mgr ;
		this.cacheName = cacheName ;
	}

	@Override
	public void put(Object key, Object value) {
		manager.getCache(cacheName).put(new Element(key,value));
	}

	@Override
	public boolean putIfAbsent(Object key, Object value) {
		Element e = manager.getCache(cacheName).putIfAbsent(new Element(key,value));
		return e==null ;
	}
	
	@Override
	public Object putIfAbsentWithValue(Object key, Object value) {
		Element e = manager.getCache(cacheName).putIfAbsent(new Element(key,value));
		return e==null ? null : e.getObjectValue() ;
	}

	@Override
	public Object get(Object key) {
		Element e = manager.getCache(cacheName).get(key);
		return e==null ? null : e.getObjectValue() ;
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getKeys() {
		return manager.getCache(cacheName).getKeys() ;
	}

	@Override
	public boolean remove(Object key) {
		return manager.getCache(cacheName).remove(key);
	}

	@Override
	public void removeAll() {
		manager.getCache(cacheName).removeAll();
	}

	@Override
	public Object getDelegate() {
		return manager ;
	}
	
	
	
}
