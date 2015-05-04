package com.chttl.jee.core.ejb;

import java.lang.reflect.Method;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Session Bean implementation class TransactionService
 */
@Stateless
public class TransactionService implements TransactionServiceRemote {
    
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Object invokeRequired(Class<?> targetClass,Signature signature) throws Exception{
    	return invoke(targetClass,signature);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Object invokeRequiresNew(Class<?> targetClass, Signature signature)throws Exception {
		return invoke(targetClass,signature);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Object invokeNotSupported(Class<?> targetClass, Signature signature)throws Exception {
		return invoke(targetClass,signature);
	}

	//really invoke
	private Object invoke(Class<?> targetClass,Signature signature)throws Exception{
    	String methodName = signature.getMethodName() ;
		Object[] args = signature.unzipArgs() ;
		Class<?>[] argTypes = Signature.extractArgTypes(args);
		Method method = targetClass.getMethod(methodName, argTypes);
		return method.invoke(targetClass.newInstance(), args);
    }
	
}
