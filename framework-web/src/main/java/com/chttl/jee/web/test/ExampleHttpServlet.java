package com.chttl.jee.web.test;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.chttl.jee.core.util.ServiceUtil;
import com.chttl.jee.rule.core.IRuleEngine;
import com.chttl.jee.rule.core.RuleContext;
import com.chttl.jee.test.ExampleBO;


@WebServlet(value="/helloworld",
initParams = {
   @WebInitParam(name="foo", value="Hello "),
   @WebInitParam(name="bar", value=" World!")
})
public class ExampleHttpServlet extends GenericServlet  {
	
	private static final long serialVersionUID = 1L;

	public void service(ServletRequest req, ServletResponse res)
			throws IOException, ServletException
	{
		PrintWriter out = res.getWriter();
		out.println(getInitParameter("foo"));
		out.println(getInitParameter("bar")) ;

		IRuleEngine engine = (IRuleEngine) ServiceUtil.getService("ruleEngine");
		System.out.println(engine);

		
		engine.fireAllRules(new RuleContext(){});
		
		System.out.println("servlet:"+ServiceUtil.getService("cacheManager"));
		
		ExampleBO bo = (ExampleBO) ServiceUtil.getService("test");
		bo.testExample(); 

		System.out.println("done") ;

	}

}