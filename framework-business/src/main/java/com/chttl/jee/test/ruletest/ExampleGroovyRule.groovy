package com.chttl.jee.test.ruletest

import com.chttl.jee.rule.annotations.GroovyRule
import com.chttl.jee.rule.annotations.RuleFunction
import com.chttl.jee.rule.core.RuleContext


@GroovyRule
class ExampleGroovyRule {

	@Delegate RuleContext context
	
	@RuleFunction
	public def rule001={
		
		comment{ "測試" }
		mode{ "測試" }
		when{
			1 == 1
		}
		execute{
			println "hello world"
		}
		
	}
	
}
