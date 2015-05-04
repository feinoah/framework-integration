package com.chttl.jee.core.ejb;

import javax.ejb.Remote;

@Remote
public interface TransactionServiceRemote {

	public Object invokeRequired(Class<?> targetClass,Signature signature) throws Exception;
	public Object invokeRequiresNew(Class<?> targetClass,Signature signature) throws Exception;
	public Object invokeNotSupported(Class<?> targetClass,Signature signature) throws Exception;

}
