package com.chttl.jee.dao.spi;

import javax.persistence.EntityManagerFactory;
import com.chttl.jee.core.util.ServiceUtil;

public final class JpaPersistence {

	public static EntityManagerFactory createEntityManagerFactory(final String persistenceUnitBeanName) {
		EntityManagerFactory emf = (EntityManagerFactory) ServiceUtil.getService(persistenceUnitBeanName);
		JpaEntityManagerFactory jemf = new JpaEntityManagerFactoryImpl(emf,persistenceUnitBeanName) ;
		return jemf ;

	}
	
}