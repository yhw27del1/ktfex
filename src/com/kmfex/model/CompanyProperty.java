package com.kmfex.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;  
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
 

/*
 * 公司性质
 * @author   
 */
@Entity
@Table(name = "t_CompanyProperty",schema="KT")
public class CompanyProperty implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6803049418327281647L;
	/**
	 * 人工编码
	 */
	private long id;  
	private String name;//名称
	private boolean leaf;
	private CompanyProperty parent; 
	private Set<CompanyProperty> children; 
	private Long parentId; //辅助变量不参与映射数据库
	private String note;//描述
	/**
	 * 程序自动编码规则：
	 * 一级 1
	 * 二级 1m2 
	 * 三级 1m2m3 
	 * 依此类推
	 * 唯一,最关键字段 ,体现层次 
	 */
	private String coding; 
	/**
	 * 人工编码/通用编码
	 */
	private String showCoding; 
	
	public CompanyProperty(String showCoding,String name,Long id, boolean leaf, CompanyProperty parent
			,String coding) { 
		this.id=id;
		this.name = name;
		this.leaf = leaf;
		this.parent = parent;
		this.coding=coding;
		this.showCoding = showCoding;
	}
	
	public CompanyProperty(String name, boolean leaf) { 
		this.name = name;
		this.leaf = leaf;
	}

	public CompanyProperty() {  
	}	
	
	public CompanyProperty(String name) { 
		this.name = name;
	}
	
	public CompanyProperty(long id) { 
		this.id = id;
	}
	@Id  
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="parentid_")
	public CompanyProperty getParent() {
		return parent;
	}
	public void setParent(CompanyProperty parent) {
		this.parent = parent;
	}
	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.REMOVE},mappedBy="parent")
	public Set<CompanyProperty> getChildren() {
		return children;
	}
	public void setChildren(Set<CompanyProperty> children) {
		this.children = children;
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
    @Transient
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getShowCoding() {
		return showCoding;
	}
	public void setShowCoding(String showCoding) {
		this.showCoding = showCoding;
	}
	
}
