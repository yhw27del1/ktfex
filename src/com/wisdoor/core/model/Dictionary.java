package com.wisdoor.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 字段表
 * @author   
 */

@Entity
@Table(name="sys_Dictionary",schema="KT")
public class Dictionary {  
	private String id; 
	/**名称 **/
	private String name;  
	/**多项值,json **/
	private String otherValues;
	/**父级点**/
	private String parent;
	/**备注**/
	private String note;
	
	
	
	public Dictionary() { 
	}
	
	
	public Dictionary(String id, String name, String otherValues, String parent) {
		this.id = id;
		this.name = name;
		this.otherValues = otherValues;
		this.parent = parent;
	}


	public Dictionary(String id, String name, String parent) { 
		this.id = id;
		this.name = name;
		this.parent = parent;
	}


	public Dictionary(String id, String name) { 
		this.id = id;
		this.name = name;
	}
	@Id 
	@Column(length=80)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="name_",length=80)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOtherValues() {
		return otherValues;
	}
	public void setOtherValues(String otherValues) {
		this.otherValues = otherValues;
	}
	@Column(name="parent_",length=80)
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}

	@Column(name="note_")
	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	} 
	 
 
}
