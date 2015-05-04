package com.chttl.jee.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.chttl.jee.cache.util.MultiValueKey;
import com.chttl.jee.cache.util.MultiValueMapUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EntityCacheDaoImpl extends JpaDao implements EntityCacheDao{

	public EntityCacheDaoImpl(String persisteceBeanName) {
		super(persisteceBeanName);
	}

	@Override
	public <T> String code2Name(Class<T> entity, String codeFieldName,String valueFieldName, String code) {
		MultiValueMap<MultiValueKey, String> tb = getCodeTable(entity, codeFieldName, valueFieldName) ;
		return MultiValueMapUtil.<String>of(tb).getSingle(code);
	}

	@Override
	public <T> String code2Name(Class<T> entity, String[] codeFieldNames,String valueFieldName, String[] codes) {
		MultiValueMap<MultiValueKey, String> tb = getCodeTable(entity, codeFieldNames, valueFieldName) ;
		return MultiValueMapUtil.<String>of(tb).getSingle(MultiValueKey.of(codes));
	}

	@Override
	public <T> MultiValueMap<MultiValueKey, Object> getTable(Class<T> entity,String keyFieldName) {
		return getTable(entity,new String[]{keyFieldName});
	}

	@Override
	public <T> MultiValueMap<MultiValueKey, Object> getTable(Class<T> entity,String... keyFieldNames) {
		//select t from table t order by keyFieldNames
		MultiValueMap<MultiValueKey, Object> map = new LinkedMultiValueMap<MultiValueKey, Object>() ;
		
		StringBuilder sb = new StringBuilder("SELECT t FROM ") ;
		sb.append(entity.getSimpleName());
		sb.append(" t order by ") ;
		for(int i=0;i<keyFieldNames.length;++i){
			sb.append("t.").append(keyFieldNames[i]);
			if(i!=keyFieldNames.length-1) sb.append(", ");
		}
		String jpql = sb.toString() ;
		Query q = em.createQuery(jpql, entity);
		List<T> list = q.getResultList() ;
		
		for(final T t : list){
			List<String> keys = new ArrayList<String>() ;
			for(String kf : keyFieldNames){
				String p = getProperties(t, kf);
				keys.add(p);
			}
			MultiValueKey key = MultiValueKey.of(keys.toArray(new String[keys.size()]));
			map.add(key, t);
		}
		
		return map ;
	}
	
	

	@Override
	public <T> MultiValueMap<MultiValueKey, String> getCodeTable(Class<T> entity, String codeFieldName, String valueFieldName) {
		return getCodeTable(entity,new String[]{codeFieldName},valueFieldName);
	}

	
	
	@Override
	public <T> MultiValueMap<MultiValueKey, String> getCodeTable(Class<T> entity, String[] codeFieldNames, String valueFieldName) {
		//select f1 as code1,f2 as code2,... c as value from table
		MultiValueMap<MultiValueKey, String> map = new LinkedMultiValueMap<MultiValueKey, String>() ;
		String tbn = resolveTableName(entity) ;
		
		StringBuilder sb = new StringBuilder("SELECT DISTINCT ") ;
		for(int i=0;i<codeFieldNames.length;++i){
			sb.append(codeFieldNames[i]).append(" AS code").append(i+1).append(", ") ;
		}
		sb.append(valueFieldName).append(" AS name FROM ").append(tbn) ;
		
		String sql = sb.toString() ;
		Query q = em.createNativeQuery(sql) ;
		
		List<Object[]> list = q.getResultList();
		
		for(Object[] objarray : list){
			String[] keyArray = Arrays.copyOf(objarray, objarray.length-1, String[].class);
			MultiValueKey key = MultiValueKey.of(keyArray); //keyArray組成一個key物件
			map.add(key, objarray[objarray.length-1].toString() );
		}
		
		return map;
	}
	
	
	private String resolveTableName(Class<?> entity){
		Table tb = entity.getAnnotation(Table.class);
		String schema = tb.schema();
		schema = (StringUtils.isEmpty(schema)) ? "" : (schema + ".");
		String tbn = schema+tb.name() ;
		return tbn ;
	}
	
	private static String getProperties(Object bean,String name){
		String ret=null;
		try {
			ret = BeanUtils.getProperty(bean, name);
		} catch (IllegalAccessException | InvocationTargetException| NoSuchMethodException e) {
		}
		return ret==null? "" : ret ;
	}

//	@Override
//	public <T> void reloadEntity(Class<T> entityClass) {
//		throw new RuntimeException("You Can not Pass!");
//	}
//
//	@Override
//	public <T> void reloadCodeEntity(Class<T> entityClass) {
//		throw new RuntimeException("You Can not Pass!");
//	}
//
//	@Override
//	public MemoryCache getMemoryCache(String cacheName) {
//		throw new RuntimeException("You Can not Pass!");
//	}

}
