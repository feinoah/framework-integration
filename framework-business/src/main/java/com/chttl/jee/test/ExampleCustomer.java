package com.chttl.jee.test;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema="test", name="customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleCustomer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id private String id ;
	private String name ;
	private Date birthday ;
	private String address ;

	public ExampleCustomer(ExampleCustomer c){
		this.id = c.id ;
		this.name = c.name ;
		this.birthday = c.birthday!=null? (Date) c.birthday.clone() : null; 
		this.address = c.address ;
	}
	
}
