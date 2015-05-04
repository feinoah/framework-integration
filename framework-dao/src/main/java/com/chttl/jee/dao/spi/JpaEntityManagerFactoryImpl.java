package com.chttl.jee.dao.spi;


import java.io.Closeable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.chttl.jee.util.threadlocal.TransactionScopeManager;

import lombok.Getter;


public final class JpaEntityManagerFactoryImpl extends JpaEntityManagerFactoryDelegate implements JpaEntityManagerFactory{

	//暫時沒有用處
	@Getter private String persistenceUnitBeanName ;
	
	public JpaEntityManagerFactoryImpl(EntityManagerFactory emf,String persistenceUnitBeanName) {
		super(emf);
		this.persistenceUnitBeanName = persistenceUnitBeanName ;
	}


	@Override
	public EntityManager createEntityManager() {
		//從同一個thread裡取EntityManager 不要每次都長新的
		JpaEntityManager em = (JpaEntityManager) TransactionScopeManager.getWithInitial(persistenceUnitBeanName) ;
		if (em == null || !em.isOpen()) {
			EntityManager delegate = super.createEntityManager() ;
			em = new JpaEntityManagerImpl(delegate) ;
			TransactionScopeManager.putWithInitial(persistenceUnitBeanName, (Closeable)em);
		}
		return em ;
	}
	
}