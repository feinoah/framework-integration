package com.chttl.jee.test;

import net.sf.ehcache.CacheManager;

import org.springframework.cache.ehcache.EhCacheCacheManager;

import com.chttl.jee.cache.MemoryCache;
import com.chttl.jee.cache.util.CodeManagerAPI;
import com.chttl.jee.cache.util.CodeMapperAPI;
import com.chttl.jee.cache.util.MemoryUtil;
import com.chttl.jee.core.util.ServiceUtil;
import com.chttl.jee.log.XLog;

public class ExampleBOImpl implements ExampleBO{

	
	@Override
	public String testExample() {

//		CodeMapperAPI api = MemoryUtil.getCacheAPI("cacheAPI");
//		String name = api.code2Name(ExampleCustomer.class, "id", "name", "1");
//		System.out.println(name);
//		
//		
//		CodeManagerAPI mapi = MemoryUtil.getManagerAPI("cacheAPI");
//		mapi.reloadCodeEntity(ExampleCustomer.class);
//		
//		
//		name = api.code2Name(ExampleCustomer.class, "id", "name", "1");
//		System.out.println(name);
		
//		System.out.println("BO:"+ServiceUtil.getService("cacheManager"));
//		EhCacheCacheManager ecm = (EhCacheCacheManager) ServiceUtil.getService("cacheManager") ;
//		CacheManager cm = ecm.getCacheManager();
//		System.out.println(cm);
//		CacheManager cm2 = (CacheManager) ServiceUtil.getService("ehCacheManager");
//		System.out.println(cm2);
//		MemoryCache cm3 = (MemoryCache) ServiceUtil.getService("cacheAPI");
//		System.out.println(cm3);
//		ExampleCustomerDao<String,ExampleCustomer> dao = (ExampleCustomerDao<String,ExampleCustomer>) ServiceUtil.getService("exampleDao") ;
//		System.out.println(dao);
//		ExampleCustomer c = dao.query("1", ExampleCustomer.class);
//		System.out.println(c);
		
//		XLog.info("SYSTEM", "hello {}", "world!!!");
		
		return "c" ;
		
	}
	
}
