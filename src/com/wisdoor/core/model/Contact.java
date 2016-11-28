package com.wisdoor.core.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户联系方式
 * @author   
 */
@Entity 
@Table(name="sys_Contact",schema="KT")
public class Contact  implements Serializable{   
	private static final long serialVersionUID = 1642650291899573506L;
	private long id;
	/** 地址 **/
	private String address;
	/** 邮编 **/
	private String postalcode;
	/** 座机 **/
	private String phone;
	/** 手机 **/
	private String mobile;
	/** 所属用户 **/
	private User user;
	/** 所属机构和公司 **/
	private Org org;
	private String linkMan;//联系人
	private String qq;
	private String email;
	
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Id 
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SYS_CONTACT_SE")  
    @SequenceGenerator(name="SYS_CONTACT_SE",sequenceName="SYS_CONTACT_SEQUENCE",allocationSize=1)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(length=200,name="address_")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(length=6,name="postalcode_")
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postcode) {
		this.postalcode = postcode;
	}
	@Column(length=20,name="phone_")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(length=11,name="mobile_")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@OneToOne(mappedBy="userContact",cascade=CascadeType.REFRESH)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	} 
 
	@OneToOne(mappedBy="orgContact",cascade=CascadeType.REFRESH)
    public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	 
	
}
