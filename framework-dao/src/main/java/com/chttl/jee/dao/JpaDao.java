package com.chttl.jee.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.chttl.jee.dao.spi.JpaEntityManager;
import com.chttl.jee.dao.spi.JpaPersistence;
import com.chttl.jee.dao.util.DaoUtil;


@Repository
@Scope("prototype")
public class JpaDao<K, E> implements Dao<K, E> {

	protected JpaEntityManager em;
	
	@Getter @Setter
	protected DaoUtil util ;
	
	public JpaDao(String persisteceBeanName){
		util = new DaoUtil() ;

		if(em!=null){
			this.em = (JpaEntityManager) em ;
		}else{
			EntityManagerFactory emf = JpaPersistence.createEntityManagerFactory(persisteceBeanName);
			this.em = (JpaEntityManager) emf.createEntityManager() ;
		}
		
	}
	
	@Override
	public void insert(E entity) {
		em.persist(entity);
	}

	@Override
	public E query(K id, Class<E> e) {
		return em.find(e, id);
	}

	@Override
	public void delete(E entity) {
		em.remove(entity);
	}

	@Override
	public E update(E entity) {
		return em.merge(entity);
	}

	@Override
	public void detach(E entity) {
		em.detach(entity);
	}

	@Override
	public void detachAll() {
		em.clear();
	}

	@Override
	public void flush() {
		em.flush();
	}

	@Override
	public void setAutoFlush(boolean autoFlush) {
		em.setAutoFlush(autoFlush);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> queryAll(Class<E> entity) {
		Query q = em.createQuery("select t from "+entity.getSimpleName()+" t", entity.getClass());
		return q.getResultList() ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public K insertFetchSerial(E entity) {
		E e = em.persistWithManaged(entity);
        K key = (K) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(e);
        return key;
	}

}