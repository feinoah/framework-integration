package com.chttl.jee.rule.core;

public interface IRuleEngine {

	public void defineRuleClass(Class<?> ruleClass);
	
	public void redefineRuleClass(RuleKey key, Class<?> ruleClass);
	
	public void fireAllRules(Class<?> ruleClass,RuleContext ctx) ;
	
	public void fireAllRules(RuleContext ctx);
	
	public void loadStatic();
	
	public void loadDynamic(String rulesPath) ;
	
}