package com.kmfex.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 公司行业
 * @author   
 */
@Entity
@Table(name = "t_Industry",schema="KT")
public class Industry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -345262138519095078L;
	/**
	 * 人工编码
	 */
	private long id;  
	private String name;//名称 
	private String note;//描述
	 
	public Industry() {  
	}	
	
	public Industry(long id,String name) { 
		this.name = name;
		this.id = id;
	}
	public Industry(String name) { 
		this.name = name; 
	}
	public Industry(long id) { 
		this.id = id;
	}
 

	@Id  
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
  

	@Column(name="name_")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="note_")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
}
