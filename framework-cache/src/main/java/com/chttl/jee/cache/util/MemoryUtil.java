package com.chttl.jee.cache.util;

import com.chttl.jee.cache.MemoryCache;
import com.chttl.jee.cache.MemoryLoaderTemplate;
import com.chttl.jee.core.util.ServiceUtil;

public class MemoryUtil {

	private static CodeMapperAPI remoteAPI ;
	private static CodeAPI localAPI ;
	
	public static CodeMapperAPI getCacheAPI(String apiName){
		return getCodeAPI(apiName);
	}
	
	public static CodeManagerAPI getManagerAPI(String apiName){
		return getCodeAPI(apiName);
	}
	
	private static CodeAPI getCodeAPI(String apiName){
		initial() ;
		if(localAPI==null){
			MemoryCache cache = (MemoryCache) ServiceUtil.getService(apiName);
			localAPI = new MemoryLoaderTemplate(cache,remoteAPI) ;
		}
		return localAPI ;
	}
	
	private static void initial(){
		if(remoteAPI!=null) return ;
		remoteAPI = (CodeMapperAPI) ServiceUtil.getService("memoryLoader") ;
	}

	
}
