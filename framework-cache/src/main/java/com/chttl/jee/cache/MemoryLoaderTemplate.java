package com.chttl.jee.cache;

import java.util.Iterator;
import java.util.List;

import org.springframework.util.MultiValueMap;

import com.chttl.jee.cache.util.CodeAPI;
import com.chttl.jee.cache.util.CodeMapperAPI;
import com.chttl.jee.cache.util.MultiValueKey;
import com.chttl.jee.cache.util.MultiValueMapUtil;

@SuppressWarnings("unchecked")
public class MemoryLoaderTemplate implements CodeAPI{

	private CodeMapperAPI mapper ;
	private MemoryCache cache ;
	
	public MemoryLoaderTemplate(MemoryCache cache,CodeMapperAPI mapper){
		this.mapper = mapper ;
		this.cache = cache ;
	}

	@Override
	public <T> String code2Name(Class<T> entity, String codeFieldName, String valueFieldName, String code) {
		MultiValueMap<MultiValueKey, String> map = getCodeTable(entity, codeFieldName, valueFieldName);
		return MultiValueMapUtil.<String>of(map).getSingle(code) ;
		//此function使用頻率極高, 不要製造太多 new String[], 直接呼叫使用
		//return code2Name(entity,new String[]{codeFieldName},valueFieldName,new String[]{code}) ;
	}

	@Override
	public <T> String code2Name(Class<T> entity, String[] codeFieldNames, String valueFieldName, String[] codes) {
		MultiValueMap<MultiValueKey, String> map = getCodeTable(entity, codeFieldNames, valueFieldName);
		return MultiValueMapUtil.<String>of(map).getSingle(MultiValueKey.of(codes)) ;
	}

	@Override
	public <T> MultiValueMap<MultiValueKey, Object> getTable(Class<T> entity, String keyFieldName) {
		return getTable(entity,new String[]{keyFieldName});
	}

	@Override
	public <T> MultiValueMap<MultiValueKey, Object> getTable(Class<T> entity, String... keyFieldNames) {

		String entityName = entity.getSimpleName();
		String[] keys = new String[keyFieldNames.length+2];
		keys[0] = "gettable"; keys[1] = entityName ;
		for(int i=2;i<keys.length;++i){
			keys[i] = keyFieldNames[i-2];
		}
		
		MultiValueKey key = MultiValueKey.of(keys) ;
		
		MultiValueMap<MultiValueKey,Object> map = (MultiValueMap<MultiValueKey, Object>) cache.get(key);
		if(map!=null) return map ;
		
		map = mapper.getTable(entity, keyFieldNames);
		cache.put(key, map);
		
		return map;
		
	}

	@Override
	public <T> MultiValueMap<MultiValueKey, String> getCodeTable(Class<T> entity, String codeFieldName, String valueFieldName) {
		return getCodeTable(entity,new String[]{codeFieldName},valueFieldName);
	}

	@Override
	public <T> MultiValueMap<MultiValueKey, String> getCodeTable(Class<T> entity, String[] codeFieldNames, String valueFieldName) {
		
		String entityName = entity.getSimpleName();

		String[] keys = new String[codeFieldNames.length+3];
		keys[0] = "getcodetable"; keys[1] = entityName ; keys[codeFieldNames.length+2] = valueFieldName ;
		for(int i=2;i<keys.length-1;++i){
			keys[i] = codeFieldNames[i-2];
		}
		MultiValueKey key = MultiValueKey.of(keys);
		
		MultiValueMap<MultiValueKey, String> map = (MultiValueMap<MultiValueKey, String>) cache.get(key);
		if(map!=null) return map ;
		
		map = mapper.getCodeTable(entity, codeFieldNames, valueFieldName);
		cache.put(key, map);
		
		return map ;
	}

	@Override
	public <T> void reloadEntity(Class<T> entityClass) {
		List<Object> list = cache.getKeys();
		String entityName = entityClass.getSimpleName() ;
		for(Object obj : list){
			MultiValueKey key = (MultiValueKey) obj ;  //gettable#ExampleCustomer#id
			String functionKey = key.fetchIndexKey(0) ;
			String entityKey = key.fetchIndexKey(1) ;
			if("gettable".equals(functionKey) && entityName.equals(entityKey)){
				String[] keyFieldNames = copyStringArray(key.getKeys(),2,key.size()-1);
				MultiValueMap<MultiValueKey,Object> mapReload = mapper.getTable(entityClass, keyFieldNames) ;
				MultiValueMap<MultiValueKey,Object> mapInCache = (MultiValueMap<MultiValueKey, Object>) cache.get(key);
				
				Iterator<MultiValueKey> itr = mapReload.keySet().iterator();
				while(itr.hasNext()){
					MultiValueKey k = itr.next() ;
					mapInCache.replace(k, mapReload.get(k)) ;
				}
				
			}
		}
	}
	
	@Override
	public <T> void reloadCodeEntity(Class<T> entityClass) {
		List<Object> list = cache.getKeys();
		String entityName = entityClass.getSimpleName() ;
		for(Object obj : list){
			MultiValueKey key = (MultiValueKey) obj ;  //getcodetable#entityName#codeFieldName#valueFieldName
			String functionKey = key.fetchIndexKey(0) ;
			String entityKey = key.fetchIndexKey(1) ;
			if("getcodetable".equals(functionKey) && entityName.equals(entityKey)){
				String[] keyFieldNames = copyStringArray(key.getKeys(),2,key.size()-2);
				String valueFieldName = key.getKeys()[key.size()-1];
				
				MultiValueMap<MultiValueKey, String> mapReload = mapper.getCodeTable(entityClass, keyFieldNames, valueFieldName);
				MultiValueMap<MultiValueKey, String> mapInCache = (MultiValueMap<MultiValueKey, String>) cache.get(key);
				
				Iterator<MultiValueKey> itr = mapReload.keySet().iterator();
				while(itr.hasNext()){
					MultiValueKey k = itr.next() ;
					mapInCache.replace(k, mapReload.get(k)) ;
				}
				
			}
		}
		
	}


	@Override
	public MemoryCache getMemoryCache(String cacheName) {
		return cache ;
	}
	
	
	private static String[] copyStringArray(String[] target, int start, int end){
		String[] to = new String[end-start+1] ;
		for(int i=0;i<end-start+1;++i){
			to[i] = target[i+start] ;
		}
		return to ;
	}
	
}
