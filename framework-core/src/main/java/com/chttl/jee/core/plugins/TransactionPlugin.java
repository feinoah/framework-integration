package com.chttl.jee.core.plugins;

import java.lang.reflect.Method;

import lombok.Data;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import com.chttl.jee.core.ejb.Signature;
import com.chttl.jee.core.ejb.TransactionServiceRemote;
import com.chttl.jee.util.transaction.PropagationMode;
import com.chttl.jee.util.transaction.TransactionSupport;

/**
 * 
 * 經過 com.chttl.je.business 下的所有類別所有方法都會包覆交易功能
 *
 */

@Aspect
@Data
@Order(1)
public final class TransactionPlugin {

	private TransactionServiceRemote delegate ;
	
//	@Pointcut("")
//	public void scope(){};
	
	//預設為 com.chttl.jee.business下的所有方法都會應用此交易處理,若要自訂則可自行定義  META-INF/aop.xml
	@Around("execution( * com.chttl.jee.test.*.*(..) )")
	public Object transactionIt(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("Transaction Plugin Appling...");
		try{
			Class<?> clazz = joinPoint.getTarget().getClass() ;
			String methodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs() ;
			Signature signature = new Signature(methodName,args);
			
			Class<?>[] argTypes = Signature.extractArgTypes(args);
			Method method = clazz.getMethod(methodName, argTypes);
			TransactionSupport support = method.getAnnotation(TransactionSupport.class);
			if(support!=null){
				System.out.println("Transaction Support!");
				PropagationMode mode = support.mode();
				if(mode==PropagationMode.REQUIRED) return delegate.invokeRequired(clazz, signature);
				else if(mode==PropagationMode.REQUIRES_NEW) return delegate.invokeRequiresNew(clazz, signature);
				else if(mode==PropagationMode.NOT_SUPPORTED) return delegate.invokeNotSupported(clazz, signature);
				throw new RuntimeException("Unexpected Propagation Mode:"+mode+", must be in [REQUIRED, REQUIRES_NEW, NOT_SUPPORTRD]");
			}else{
				System.out.println("Pure POJO Execution!");
				return method.invoke(clazz.newInstance(), args);
			}
			
			
		}finally{
			System.out.println("Transaction Plugin Removing...");
		}
	}
	
	
}
