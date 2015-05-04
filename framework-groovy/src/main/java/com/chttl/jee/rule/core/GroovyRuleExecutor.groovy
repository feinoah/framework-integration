package com.chttl.jee.rule.core

import groovy.lang.Closure;

class GroovyRuleExecutor{

	boolean ret = false
	
	def comment(Closure c){
		return c()
	}
	
	def mode(Closure c){
		return c()
	}
	
	def when(Closure c){
		ret = c()
	}
	
	def execute(Closure c){
		if(ret) c()
	}

	
}
