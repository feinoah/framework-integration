package com.chttl.jee.rule.core;

public interface RuleSpec {

	public String comment() ;
	
	public String mode() ;
	
	public boolean when() ;
	
	public void execute() ;
	
}