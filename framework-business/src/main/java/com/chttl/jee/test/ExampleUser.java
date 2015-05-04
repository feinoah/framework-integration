package com.chttl.jee.test;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema="test", name="user")
public class ExampleUser implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id private Integer userid ;
	private String status ;
	private String orders ;

}
