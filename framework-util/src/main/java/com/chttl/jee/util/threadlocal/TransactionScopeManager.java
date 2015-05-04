package com.chttl.jee.util.threadlocal;

import java.io.Closeable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public final class TransactionScopeManager{

	private static ThreadLocal<ConcurrentHashMap<String, Closeable>> threadLocal = new ThreadLocal<ConcurrentHashMap<String, Closeable>>();
	
	
	public static Object getWithInitial(String key){
		ConcurrentHashMap<String, Closeable> map = threadLocal.get();
		if(map==null){
			threadLocal.set(new ConcurrentHashMap<String,Closeable>());
			return null ;
		}else{
			return map.get(key) ;
		}
	}
	
	public static void putWithInitial(String key,Closeable obj){
		ConcurrentHashMap<String, Closeable> map = threadLocal.get();
		if(map==null) threadLocal.set(new ConcurrentHashMap<String,Closeable>());
		map.put(key, obj);
	}
	
	public static void closeAllWithRemove(){
		ConcurrentHashMap<String, Closeable> map = threadLocal.get();
		if(map==null) return ;
		Iterator<Closeable> itr = map.values().iterator() ;
		while(itr.hasNext()){
			try {
				Closeable c = itr.next();
				c.close();
				System.out.println("Resource:"+c+" has been closed");
			} catch (Exception e) {
				System.out.println("Close Resource Fail:"+e.getMessage()+", It may cause memory leak!");
			}
		}
		System.out.println("Resource:"+threadLocal+" has been removed");
		threadLocal.remove();
		
	}
	
}