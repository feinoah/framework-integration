package com.chttl.jee.dao;

import java.util.List;

public interface Dao<K,E> {

	
	void insert(E entity);
	
	E query(K id,Class<E> e);
	
    void delete(E entity);
    
    public E update(E entity);

    public void detach(E entity) ;
    
    public void detachAll() ;
    
    public void flush();
    
    public void setAutoFlush(boolean autoFlush);
    
    public List<E> queryAll(Class<E> entity) ;

    public K insertFetchSerial (E entity);

	
}