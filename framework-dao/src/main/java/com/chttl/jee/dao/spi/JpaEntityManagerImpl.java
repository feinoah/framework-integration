package com.chttl.jee.dao.spi;

import java.io.Closeable;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import com.chttl.jee.dao.util.DaoUtil;
import com.chttl.jee.util.io.ObjectSerializer;

public final class JpaEntityManagerImpl implements JpaEntityManager,Closeable{

	protected boolean autoFlush ;
	protected EntityManager delegate ;
	protected DaoUtil util ;
	
	public JpaEntityManagerImpl(EntityManager delegate) {
		this.delegate = delegate ;
		this.util = new DaoUtil() ;
	}

	@Override
	public void setAutoFlush(boolean auto) {
		this.autoFlush = auto ;
	}

	public void persist(Object entity) {
		Object e = util.encodeToDBString(ObjectSerializer.cloneBySerialize(entity));
		delegate.persist(e);
	}
	
	public <T> T persistWithManaged(T entity){
		T e = util.encodeToDBString(ObjectSerializer.cloneBySerialize(entity));
		delegate.persist(e);
		return e ;
	}

	public <T> T merge(T entity) {
		T e = util.encodeToDBString(ObjectSerializer.cloneBySerialize(entity));
		delegate.merge(e);
		return entity ;
	}

	public void remove(Object entity) {
		delegate.remove(entity);
	}

	public <T> T find(Class<T> entityClass, Object primaryKey) {
		T ret = delegate.find(entityClass, primaryKey);
		T cloneE = ObjectSerializer.cloneBySerialize(util.encodeToAPString(ret));
		return cloneE ;
	}

	public <T> T find(Class<T> entityClass, Object primaryKey,Map<String, Object> properties) {
		T ret = delegate.find(entityClass, primaryKey, properties);
		T cloneE = ObjectSerializer.cloneBySerialize(util.encodeToAPString(ret));
		return cloneE ;
	}

	public <T> T find(Class<T> entityClass, Object primaryKey,LockModeType lockMode) {
		T ret = delegate.find(entityClass, primaryKey, lockMode);
		T cloneE = ObjectSerializer.cloneBySerialize(util.encodeToAPString(ret));
		return cloneE ;
	}

	public <T> T find(Class<T> entityClass, Object primaryKey,LockModeType lockMode, Map<String, Object> properties) {
		T ret = delegate.find(entityClass, primaryKey, lockMode, properties);
		T cloneE = ObjectSerializer.cloneBySerialize(util.encodeToAPString(ret));
		return cloneE ;
	}

	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return delegate.getReference(entityClass, primaryKey);
	}

	public void flush() {
		delegate.flush();
	}

	public void setFlushMode(FlushModeType flushMode) {
		delegate.setFlushMode(flushMode);
	}

	public FlushModeType getFlushMode() {
		return delegate.getFlushMode();
	}

	public void lock(Object entity, LockModeType lockMode) {
		delegate.lock(entity, lockMode);
	}

	public void lock(Object entity, LockModeType lockMode,Map<String, Object> properties) {
		delegate.lock(entity, lockMode, properties);
	}

	public void refresh(Object entity) {
		delegate.refresh(entity);
	}

	public void refresh(Object entity, Map<String, Object> properties) {
		delegate.refresh(entity, properties);
	}

	public void refresh(Object entity, LockModeType lockMode) {
		delegate.refresh(entity, lockMode);
	}

	public void refresh(Object entity, LockModeType lockMode,Map<String, Object> properties) {
		delegate.refresh(entity, lockMode, properties);
	}

	public void clear() {
		delegate.clear();
	}

	public void detach(Object entity) {
		delegate.detach(entity);
	}

	public boolean contains(Object entity) {
		return delegate.contains(entity);
	}

	public LockModeType getLockMode(Object entity) {
		return delegate.getLockMode(entity);
	}

	public void setProperty(String propertyName, Object value) {
		delegate.setProperty(propertyName, value);
	}

	public Map<String, Object> getProperties() {
		return delegate.getProperties();
	}

	public Query createQuery(String qlString) {
		Query q = delegate.createQuery(qlString);
		return new JpaQuery(q,qlString);
	}

	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return delegate.createQuery(criteriaQuery);
	}

	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		return delegate.createQuery(qlString, resultClass);
	}

	public Query createNamedQuery(String name) {
		Query q = delegate.createNamedQuery(name);
		return new JpaQuery(q,name);
	}

	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		return delegate.createNamedQuery(name, resultClass);
	}

	public Query createNativeQuery(String sqlString) {
		Query q = delegate.createNativeQuery(sqlString);
		return new JpaQuery(q,sqlString);
	}

	@SuppressWarnings("rawtypes")
	public Query createNativeQuery(String sqlString, Class resultClass) {
		Query q = delegate.createNativeQuery(sqlString, resultClass);
		return new JpaQuery(q,sqlString);
	}

	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		Query q = delegate.createNativeQuery(sqlString, resultSetMapping);
		return new JpaQuery(q,sqlString);
	}

	public void joinTransaction() {
		delegate.joinTransaction();
	}

	public <T> T unwrap(Class<T> cls) {
		return delegate.unwrap(cls);
	}

	public Object getDelegate() {
		return delegate.getDelegate();
	}

	public void close() {
		delegate.close();
	}

	public boolean isOpen() {
		return delegate.isOpen();
	}

	public EntityTransaction getTransaction() {
		return delegate.getTransaction();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return delegate.getEntityManagerFactory();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return delegate.getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		return delegate.getMetamodel();
	}

}