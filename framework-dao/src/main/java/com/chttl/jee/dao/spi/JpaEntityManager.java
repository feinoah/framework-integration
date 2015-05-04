package com.chttl.jee.dao.spi;

import javax.persistence.EntityManager;

public interface JpaEntityManager extends EntityManager{

	public void setAutoFlush(boolean auto);
	
	public <T> T persistWithManaged(T entity) ; 
	
}