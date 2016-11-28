package com.wisdoor.core.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity; 
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 功能、菜单、频道、栏目表（前台栏目频道个人企业后台菜单）(可以暂时人工维护)
 * @author   
 */

@Entity
@Table(name="sys_menu",schema="KT")
public class Menu {  
	private long id;
	/**权限字符串--必须唯一 **/
	private String privilegeValue; 
	/**名称 **/
	private String name; 
	/**图标 **/
	private String image; 
	/**class分类link链接button按钮 **/
	private String type;  
	/**链接 **/
	private String href;
	/**
	 * 编码规则：
	 * 一级 1
	 * 二级 1m2 
	 * 三级 1m2m3 
	 * 依此类推
	 * 唯一,最关键字段 ,体现层次 
	 */
	private String coding; 
	/**
	 *  显示名称(导航体现层次)
	 *  例如:首页》新闻中心》国内新闻
	 */
	private String showName; 
	/** 是否可见 **/
	@Column(name="visible_",columnDefinition="bit default 1")
	private boolean visible = true; 
	/** 是否叶子**/ 
	@Column(name="leaf_",columnDefinition="bit default 1")
	private boolean leaf = true;
	/**  同级的排序*   */
	private Integer order=1;  
	/** 子权限 **/
	private Set<Menu> childMenus = new HashSet<Menu>();  
	/** 所属父类 **/
	private Menu parent;
	 

	public Menu(long id) { 
		this.id = id;
	}

	public Menu(long id,String name, String coding,String privilegeValue, Menu parent,String type,boolean leaf) { 
		this.id = id;
		this.name = name;
		this.privilegeValue = privilegeValue; //权限字符串必须唯一 
		this.coding = coding;
		this.parent = parent;
		this.type=type;
		this.leaf=leaf;
	}
	/**
	 * 增加了目标地址
	 * @param id
	 * @param name
	 * @param coding
	 * @param href
	 * @param privilegeValue
	 * @param parent
	 * @param type
	 * @param leaf
	 */
	public Menu(long id,String name, String coding,String href,String privilegeValue, Menu parent,String type,boolean leaf) { 
		this.id = id;
		this.name = name;
		this.privilegeValue = privilegeValue; //权限字符串必须唯一 
		this.coding = coding;
		this.parent = parent;
		this.type=type;
		this.leaf=leaf;
		this.href = href;
	}

	@Id @Column(length=50)  
	public long getId() {
		return id;
	}

 
	public void setId(long id) {
		this.id = id;
	}

	@Column(name="image_")
	public String getImage() {
		return image;
	}



	public void setImage(String image) {
		this.image = image;
	}

	@Column(name="href_")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@OneToMany(cascade={CascadeType.REFRESH,CascadeType.REMOVE},mappedBy="parent")
	public Set<Menu> getChildMenus() {
		return childMenus;
	}


	public void setChildMenus(Set<Menu> childMenus) {
		this.childMenus = childMenus;
	}
 
 
	@Column(name="order_",nullable=false)
	public Integer getOrder() {
		return order;
	}


	public void setOrder(Integer order) {
		this.order = order;
	}

	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="parentid_")
	public Menu getParent() {
		return parent;
	}


	public void setParent(Menu parent) {
		this.parent = parent;
	}
 
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public Menu(){}
	

	@Column(name="name_",length=50)
	public String getName() {
		return name; 
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="type_",length=50) 
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name="privilegeValue",length=100)
	public String getPrivilegeValue() {
		return privilegeValue;
	}

	public void setPrivilegeValue(String privilegeValue) {
		this.privilegeValue = privilegeValue;
	}
 
	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
 
}
