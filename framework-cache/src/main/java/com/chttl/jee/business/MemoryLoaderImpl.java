package com.chttl.jee.business;

import org.springframework.util.MultiValueMap;

import com.chttl.jee.cache.util.MultiValueKey;
import com.chttl.jee.core.util.ServiceUtil;
import com.chttl.jee.dao.EntityCacheDao;
import com.chttl.jee.util.transaction.PropagationMode;
import com.chttl.jee.util.transaction.TransactionSupport;


public class MemoryLoaderImpl implements MemoryLoader {

	@Override
	@TransactionSupport(mode=PropagationMode.NOT_SUPPORTED)
	public <T> String code2Name(Class<T> entity, String codeFieldName,String valueFieldName, String code) {
		EntityCacheDao dao = (EntityCacheDao) ServiceUtil.getService("entityCacheDao");
		return dao.code2Name(entity, codeFieldName, valueFieldName, code);
	}

	@Override
	@TransactionSupport(mode=PropagationMode.NOT_SUPPORTED)
	public <T> String code2Name(Class<T> entity, String[] codeFieldNames,String valueFieldName, String[] codes) {
		EntityCacheDao dao = (EntityCacheDao) ServiceUtil.getService("entityCacheDao");
		return dao.code2Name(entity, codeFieldNames, valueFieldName, codes) ;
	}

	@Override
	@TransactionSupport(mode=PropagationMode.NOT_SUPPORTED)
	public <T> MultiValueMap<MultiValueKey, Object> getTable(Class<T> entity,String keyFieldName) {
		EntityCacheDao dao = (EntityCacheDao) ServiceUtil.getService("entityCacheDao");
		return dao.getTable(entity, keyFieldName) ;
	}

	@Override
	@TransactionSupport(mode=PropagationMode.NOT_SUPPORTED)
	public <T> MultiValueMap<MultiValueKey, Object> getTable(Class<T> entity,String... keyFieldNames) {
		EntityCacheDao dao = (EntityCacheDao) ServiceUtil.getService("entityCacheDao");
		return dao.getTable(entity, keyFieldNames) ;
	}

	@Override
	@TransactionSupport(mode=PropagationMode.NOT_SUPPORTED)
	public <T> MultiValueMap<MultiValueKey, String> getCodeTable(Class<T> entity, String codeFieldName, String valueFieldName) {
		EntityCacheDao dao = (EntityCacheDao) ServiceUtil.getService("entityCacheDao");
		return dao.getCodeTable(entity, codeFieldName, valueFieldName) ;
	}

	@Override
	@TransactionSupport(mode=PropagationMode.NOT_SUPPORTED)
	public <T> MultiValueMap<MultiValueKey, String> getCodeTable(Class<T> entity, String[] codeFieldNames, String valueFieldName) {
		EntityCacheDao dao = (EntityCacheDao) ServiceUtil.getService("entityCacheDao");
		return dao.getCodeTable(entity, codeFieldNames, valueFieldName);
	}


}
