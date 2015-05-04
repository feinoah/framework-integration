package com.chttl.jee.rule.core

import java.lang.reflect.Field
import java.lang.reflect.Method

import org.codehaus.groovy.control.CompilerConfiguration
import com.chttl.jee.rule.annotations.*
import com.chttl.jee.util.io.AnnotationScanner

class GroovyRuleEngine implements IRuleEngine {

	//CustomerRule.rule001 ==> public Object def rule001
	//AdslRule.rule001 ==> public RuleSpec method()
	private Map<RuleKey,Object> repository = new TreeMap<RuleKey,Object>()
	//CustomerRule ==> com.chttl.jee.rule.CustomerRule
	private Map<String,Class> classMap = [:]
	
	private String engineName 
	private List<String> scanPackage = [] 
	GroovyClassLoader loader = null
	RuleExceptionHandler exceptionHandler 
	
	public GroovyRuleEngine(String engineName, List<String> scanPackage){
		this.engineName = engineName 
		this.scanPackage.addAll(scanPackage) 
	}
	
	@Override
	public void defineRuleClass(Class ruleClass){
		String simpleClassName = ruleClass.simpleName

		ruleClass.declaredFields.each { f->
			RuleFunction function = f.getAnnotation(RuleFunction.class)
			if(function){
				RuleKey k = new RuleKey(simpleClassName,f.name,"Groovy")
				repository.put(k, f)
				classMap.put(simpleClassName, ruleClass)
			}
		}

		ruleClass.declaredMethods.each { m->
			RuleFunction function = m.getAnnotation(RuleFunction.class)
			if(function){
				RuleKey k = new RuleKey(simpleClassName,m.name,"Java")
				repository.put(k, m)
				classMap.put(simpleClassName, ruleClass)
			}
		}
		
	}
	
	@Override
	public void redefineRuleClass(RuleKey key, Class ruleClass){
		repository.remove(key)
		classMap.remove(key.className)
		defineRuleClass(ruleClass)
	}
	
	@Override
	public void fireAllRules(Class ruleClass,RuleContext ctx){
		repository.findAll { kp ->
			kp.key.className == ruleClass.simpleName
		}.each { kp ->
		    fireRule(ruleClass,kp.key.ruleName,kp.key.ruleType,ctx)
		}
	}
	
	@Override
	public void fireAllRules(RuleContext ctx){
		GroovyRuleExecutor exec = new GroovyRuleExecutor()
		repository.each { kp->
			fireRule(classMap[kp.key.className],kp.key.ruleName,kp.key.ruleType,ctx)
		}
	}
	
	private void fireRule(Class ruleClass,String ruleName,String ruleType,RuleContext ctx){
		try{
			handle(ruleClass,ruleName,ruleType,ctx)	
		}catch(Exception e){
			if(exceptionHandler) exceptionHandler.handle(ruleClass, ruleName, ruleType, ctx, e)
			//TODO else bypass rule and log it
		}
	}
	
	private void handle(Class ruleClass,String ruleName,String ruleType,RuleContext ctx){
		
		if(ruleType == "Groovy"){
			Object gRule = repository[new RuleKey(ruleClass.simpleName,ruleName,ruleType)]
			if(gRule){
				def ruleClassInst = ruleClass.newInstance()
				Closure c = gRule.get(ruleClassInst)
				c.delegate = new GroovyRuleExecutor()
				c.context = ctx 
				c()
				return
			}
		}
		else if(ruleType == "Java"){
			Object jRule = repository[new RuleKey(ruleClass.simpleName,ruleName,ruleType)]
			if(jRule){
				def ruleClassInst = ruleClass.newInstance()
				def spec = jRule.invoke(ruleClassInst,null)
				ruleClassInst.context = ctx
				Closure c = {
					comment()
					mode()
					if(when()) execute()
				}
				c.delegate = spec
				c()
				return
			}
		}
	}
	
	@Override
	public void loadStatic(){
		println "loadStatic"
		scanPackage.each { pkg ->
			println "scanning package:${pkg}"
			AnnotationScanner scanner = new AnnotationScanner(pkg)
			
			def gRuleClasses = scanner.getAllClassAnnotated(GroovyRule.class)
			gRuleClasses.each {
				println "define groovy:${it}"
				defineRuleClass(it)
			}
			
			def jRuleClasses = scanner.getAllClassAnnotated(JavaRule.class)
			jRuleClasses.each {
				println "define java:${it}"
				defineRuleClass(it)
			}
		}
	}
	
	@Override
	public void loadDynamic(String rulesPath){
		File dir = new File(rulesPath);
		if (! dir.isDirectory())
			return;
			
		ClassLoader parent = Thread.currentThread().getContextClassLoader()
		
		if(loader==null){
			CompilerConfiguration config = new CompilerConfiguration()
			config.setSourceEncoding("UTF-8")
			loader = new GroovyClassLoader(parent, config)
			loader.setShouldRecompile(Boolean.TRUE)
		}
		
		dir.listFiles().each { file ->
			GroovyCodeSource codeSource = new GroovyCodeSource(file, "UTF-8")
			Class<?> srcClass = loader.parseClass(codeSource, false)
			
			GroovyRule g = srcClass.getAnnotation(GroovyRule.class)
			if(g!=null){
				println codeSource.getScriptText()
				defineRuleClass(srcClass)
			}
			JavaRule j = srcClass.getAnnotation(JavaRule.class)
			if(j!=null){
				println codeSource.getScriptText()
				defineRuleClass(srcClass)
			}
			
		}
		
	}
	
}
