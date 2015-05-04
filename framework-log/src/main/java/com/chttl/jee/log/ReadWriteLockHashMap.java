package com.chttl.jee.log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockHashMap <K, V> implements Serializable{

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** session's employee object cache for creating new session under the same login */
	public Map<K, V> dataMap = new HashMap<K, V>();

	/** lockers for 讀寫 sessionEmployee map */
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock read = readWriteLock.readLock();
	private final Lock write = readWriteLock.writeLock();

	/** 將 data 加入 data map 中. */
	public void addData (K key, V value){
		// 將其存入 Session Map 中
		write.lock();
		try {
			dataMap.put (key, value);
		} finally {
			write.unlock();
		}
	}

	/** 自 data map 中移除指定的 key 值. */
	public boolean removeData (K key)
	{
		write.lock();
		try {
			if (dataMap.containsKey(key))
			{
				dataMap.remove(key);
				return true;
			}

			return false;
		} finally {
			write.unlock();
		}
	}

	/** 取得指定 key 的 data */
	public V getData (K key)
	{
		read.lock();
		try {
			return dataMap.get(key);
		} finally {
			read.unlock();
		}
	}

}