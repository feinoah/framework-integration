package com.chttl.jee.rule.core;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuleKey implements Comparable<RuleKey>{

	private String className ;
	private String ruleName ;
	private String ruleType;
	
	public String composite(){
		return (className+"."+ruleName) ;
	}
	
	@Override
	public int compareTo(RuleKey key) {
		if(key==null) return -1 ;
		return composite().compareTo(key.composite());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleKey other = (RuleKey) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (ruleName == null) {
			if (other.ruleName != null)
				return false;
		} else if (!ruleName.equals(other.ruleName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((ruleName == null) ? 0 : ruleName.hashCode());
		return result;
	}
	
}