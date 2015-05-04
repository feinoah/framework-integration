package com.chttl.jee.rule.core;

public interface RuleExceptionHandler {

	public boolean handle(Class<?> ruleClass,String ruleName,String ruleType,RuleContext ctx, Exception e) ;
	
}