package com.chttl.jee.test;

import com.chttl.jee.dao.JpaDao;

@SuppressWarnings("rawtypes")
public class ExampleCustomerDaoImpl extends JpaDao implements ExampleCustomerDao{

	public ExampleCustomerDaoImpl(String persisteceBeanName) {
		super(persisteceBeanName);
	}
	
}
