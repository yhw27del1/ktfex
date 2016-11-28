package com.kmfex.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 地域
 * @author eclipse
 *
 */
@Entity
@Table(name = "t_region",schema="KT")
public class Region implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7158535938330930430L;
	
	private String id;
	/**
	 * 地点编码
	 */
	private String areacode;
	/**
	 * 地点编码
	 */
	private String areaparentcode;
	
	private List<Region> children ;
	/**
	 * 显示名称(长)
	 */
	private String areaname_l;
	/**
	 * 显示名称(短)
	 */
	private String areaname_s;
	
	/**
	 * 启用状态
	 */
	private boolean enabled;
	
	/**
	 * 有子类的
	 * @param areacode
	 * @param childrencode
	 * @param areaname_l
	 * @param areaname_s
	 * @param enabled
	 */
	public Region(String areacode,String areaparentcode,String areaname_l,String areaname_s,boolean enabled){
		this.areacode = areacode;
		this.areaparentcode = areaparentcode;
		this.areaname_l = areaname_l;
		this.areaname_s = areaname_s;
		this.enabled = enabled;
	}
	public Region(){}
	
	
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getAreaname_l() {
		return areaname_l;
	}
	public void setAreaname_l(String areanameL) {
		areaname_l = areanameL;
	}
	public String getAreaname_s() {
		return areaname_s;
	}
	public void setAreaname_s(String areanameS) {
		areaname_s = areanameS;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setChildren(List<Region> children) {
		this.children = children;
	}
	@Transient
	public List<Region> getChildren() {
		return children;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}


	public void setAreaparentcode(String areaparentcode) {
		this.areaparentcode = areaparentcode;
	}


	public String getAreaparentcode() {
		return areaparentcode;
	}
}
