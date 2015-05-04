package com.chttl.jee.core.ejb;

import java.io.IOException;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import com.chttl.jee.util.io.ObjectSerializer;


public final class Signature implements Serializable{

	private static final long serialVersionUID = 1L;

	@Getter @Setter private String methodName ;
	private byte[] args ;

	public Signature(String methodName,Object[] args) throws IOException{
		this.methodName = methodName ;
		zip(args);
	}
	
	public void zip(Object[] objArgs) throws IOException{
		args = ObjectSerializer.serializeToByteArray((Object) objArgs);
	}
	
	public Object[] unzipArgs() throws ClassNotFoundException, IOException{
		return (Object[]) ObjectSerializer.deserializeToObject(this.args);
	}
	
	public static Class<?>[] extractArgTypes(Object[] args){
		if(args==null) return new Class[0] ;
		Class<?>[] argTypes = new Class[args.length] ;
		for(int i=0;i<args.length;++i){
			argTypes[i] = args[i].getClass();
		}
		return argTypes ;
	}
	
}
