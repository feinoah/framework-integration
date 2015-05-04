package com.chttl.jee.web.test;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chttl.jee.core.util.ServiceUtil;
import com.chttl.jee.test.ExampleBO;

@Component
@Scope(value="request")
@RequestScoped
@ManagedBean
public class ExampleController {

	public String getMessage(){
		
		ExampleBO bo = (ExampleBO) ServiceUtil.getService("test");
		bo.testExample(); 
		
		return "hello world";
	}
	
}