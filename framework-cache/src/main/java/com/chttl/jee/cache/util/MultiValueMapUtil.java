package com.chttl.jee.cache.util;

import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

public class MultiValueMapUtil<V>{

	private MultiValueMap<MultiValueKey, V> map ;
	private Map<MultiValueKey,V> singleValueMap ;
	
	public MultiValueMapUtil(MultiValueMap<MultiValueKey, V> map){
		this.map = map ;
		this.singleValueMap = map.toSingleValueMap() ;
	}
	
	public static <V> MultiValueMapUtil<V> of(MultiValueMap<MultiValueKey,V> map){
		return new MultiValueMapUtil<V>(map) ;
	}
	
	//String value = map.get("key")
	public Map<MultiValueKey,V> toSingleValueMap(){
		return singleValueMap ;
	}
	
	public V getSingle(String key){
		return singleValueMap.get(MultiValueKey.of(key)) ;
	}
	
	public V getSingle(MultiValueKey key){
		return singleValueMap.get(key) ;
	}
	
	public List<V> getMulit(String key){
		return map.get(MultiValueKey.of(key));
	}

}
