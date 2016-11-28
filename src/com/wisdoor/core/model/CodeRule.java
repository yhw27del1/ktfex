package com.wisdoor.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/*
 * 编码规则维护表--负责生成编码 
 * @author   
 */
@Entity
@Table(name = "sys_CodeRule", schema = "KT")
public class CodeRule implements Serializable {
	private static final long serialVersionUID = -1539734404009051270L;
	
	private String  id;
	
	/**当前最大值**/
	private long  currentMax=0;
	/**备注说明**/ 
	private String  note;
	/**2位年份**/ 
	private String  year;
     /**版本号**/
	private int version=0;
	
	
	@Id 
	@Column(length = 50)
	public String  getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public CodeRule() { 
	}
	
	public CodeRule(String id) { 
		this.id = id;
	}


	public CodeRule(String id, long currentMax, String year) { 
		this.id = id;
		this.currentMax = currentMax;
		this.year = year;
	}

 

	public CodeRule(String id, long currentMax, String year, String note ) { 
		this.id = id;
		this.currentMax = currentMax;
		this.note = note;
		this.year = year; 
	}

	public long getCurrentMax() {
		return currentMax;
	}

	public void setCurrentMax(long currentMax) {
		this.currentMax = currentMax;
	}
	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	@Column(length=100)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	@Column(length=10,name="year_")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
 

}
