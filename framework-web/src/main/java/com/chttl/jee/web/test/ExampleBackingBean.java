package com.chttl.jee.web.test;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import lombok.Data;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session")
@ManagedBean
@SessionScoped
@Data
public class ExampleBackingBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ExampleController ctrl ;

	public String showMessage(){
		return ctrl.getMessage() ;
	}
	
	private String header ="title" ;
	private String message ="Are You Sure?" ;
	
	private int action = 3 ;
	
	public void doSubmit() {
		System.out.println("======>"+action);
        action-- ;
//        message="Are You Sure?=>"+action;
//        if(action>0){
//        	RequestContext context = RequestContext.getCurrentInstance();
//    	    context.execute("PF('hiddenbtn').jq.click()");
//        }
    }

    
}
