package com.chttl.jee.cache.util;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.util.StringUtils;

public class MultiValueKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String[] keys ;
	
	public MultiValueKey(String... key){
		this.keys = key ;
	}
	
	public static MultiValueKey of(String... keys){
		return new MultiValueKey(keys);
	}
	
	public String[] getKeys(){
		return keys ;
	}
	
	public String fetchIndexKey(int index){
		if(keys==null || index>keys.length-1) return null ;
		return keys[index];
	}
	
	public int size(){
		return keys==null ? 0 : keys.length ;
	}
	
	public String toKeyString(){
		return StringUtils.arrayToCommaDelimitedString(keys); 
	}
	
	public String toKeyString(String delimeter){
		return StringUtils.arrayToDelimitedString(keys, delimeter);
	}
	
	@Override
	public String toString(){
		return toKeyString("#");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(keys);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiValueKey other = (MultiValueKey) obj;
		if (!Arrays.equals(keys, other.keys))
			return false;
		return true;
	}
	
	
	
}
