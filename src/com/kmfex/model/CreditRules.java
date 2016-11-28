package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 积分规则
 * @author eclipseeeeeeeeeeeeee
 *
 */
@Entity
@Table(name="t_creditrules",schema="KT")
public class CreditRules implements Serializable{

	private static final long serialVersionUID = 8726427991120255812L;

	
	private String id;
	/**
	 * 标题，后台用户可见
	 */
	private String title;
	
	private Date createtime = new Date();
	/**
	 * 生效时间
	 */
	private Date effecttime;
	/**
	 * 过期时间
	 */
	private Date expiretime;
/**
 * 开户积分
 */
	private int khjf;
	/**
	 * 
	 */
	
	private boolean enable = false;
	/**
	 * 关系值<br/>
	 * 如大于500
	 */
	private int relation_value = 0;
	
	/**
	 * 抵用比例
	 */
	private float value = 0f;
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 标题，后台用户可见
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 标题，后台用户可见
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 创建时间
	 * @return
	 */
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 过期时间
	 * @return
	 */
	public Date getExpiretime() {
		return expiretime;
	}
	public void setExpiretime(Date expiretime) {
		this.expiretime = expiretime;
	}
	
	/**
	  * 关系值<br/>
	 * 如大于500
	 * @return
	 */
	@Column(columnDefinition="int default 0")
	public int getRelation_value() {
		return relation_value;
	}
	/**
	 * 关系值<br/>
	 * 如大于500
	 * @param relation_value
	 */
	public void setRelation_value(int relation_value) {
		this.relation_value = relation_value;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	/**
	 * 生效时间
	 * @return
	 */
	public Date getEffecttime() {
		return effecttime;
	}
	public void setEffecttime(Date effecttime) {
		this.effecttime = effecttime;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	@Column(columnDefinition="int default 0")
	public int getKhjf() {
		return khjf;
	}
	public void setKhjf(int khjf) {
		this.khjf = khjf;
	}
	
	
	
	
}
