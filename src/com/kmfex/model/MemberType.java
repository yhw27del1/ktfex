package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 会员类型实体类
 * 
 * @author 敖汝安
 * @version 2012.03.19
 * */
@Entity
@Table(name = "t_member_types",schema="KT")
public class MemberType   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7312871773153902626L;
	/**
	 * 融资方类型代码
	 * */
	public static final String CODE_FINANCING = "R";
	/**
	 *投资方类型代码
	 * */
	public static final String CODE_INVESTORS = "T";

	/**
	 *担保方类型代码
	 * */
	public static final String CODE_SECURED = "D";

	/**
	 *银行方类型代码
	 * */
	public static final String CODE_BANK = "Y";

	/**
	 *其它类型代码
	 * */
	public static final String CODE_OTHER = "Q";

	public String id;
	/**
	 * 类型名称
	 **/
	public String name;
	/**
	 * 类型代码
	 **/
	public String code;
	
	/**
	 * 此类型的创建时间
	 **/
	public Date createDate=new Date();
	/**
	 * 此类型的修改时间
	 **/
	public Date updateDate;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberType() {  
	}
	public MemberType(String id) { 
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
