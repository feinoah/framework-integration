package com.chttl.jee.core.plugins;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import com.chttl.jee.util.threadlocal.TransactionScopeManager;

/**
 * 經過 com.chttl.jee.business 下的所有類別所有方法結束後都會自動清除 EntityManager
 *
 */

@Aspect
@Order(0)
public final class AutoClosingDaoPlugin {

	@Around("execution( * com.chttl.jee.test.*.*(..) )")
	public Object transactionIt(ProceedingJoinPoint joinPoint) throws Throwable {
		try{
			System.out.println("AutoClosingDaoPlugin Applying");
			return joinPoint.proceed() ;
		}finally{
			TransactionScopeManager.closeAllWithRemove();
			System.out.println("AutoClosingDaoPlugin Removing");
		}
		
	}
	
}
