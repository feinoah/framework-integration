package com.chttl.jee.dao.spi;

import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

public abstract class JpaEntityManagerFactoryDelegate implements EntityManagerFactory{
	
	protected EntityManagerFactory delegate ;

	public JpaEntityManagerFactoryDelegate(EntityManagerFactory delegate){
		this.delegate = delegate ;
	}

	public void close() {
		delegate.close();
	}

	public EntityManager createEntityManager() {
		return delegate.createEntityManager();
	}

	@SuppressWarnings("rawtypes") 
	public EntityManager createEntityManager(Map arg0) {
		return delegate.createEntityManager(arg0);
	}

	public Cache getCache() {
		return delegate.getCache();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return delegate.getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		return delegate.getMetamodel();
	}

	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return delegate.getPersistenceUnitUtil();
	}

	public Map<String, Object> getProperties() {
		return delegate.getProperties();
	}

	public boolean isOpen() {
		return delegate.isOpen();
	}

	
}