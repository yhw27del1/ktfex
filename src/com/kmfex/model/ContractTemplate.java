package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_contract_template", schema="KT")
public class ContractTemplate implements Serializable{
	private static final long serialVersionUID = 5055852685130047931L;
	
	private String id;
	
	private Date createtime;
	
	private String description;
	
	
	private String result_name;
	
	private String pdfpath;
	
	private String code;
	
	private float version = 0;
	/**
	 * 还款时段(1：上，-1：下午，0：无设置)
	 */
	private int hksd;
	
	private String isOverDate;//是否为过期版本
	

	/**
	 * 逾期还款滞纳金比例
	 */
	private String zhinajinbili;
	/**
	 * 提前还款违约金比例
	 */
	private String weiyuejinbili;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getResult_name() {
		return result_name;
	}

	public void setResult_name(String result_name) {
		this.result_name = result_name;
	}

	public String getPdfpath() {
		return pdfpath;
	}

	public void setPdfpath(String pdfpath) {
		this.pdfpath = pdfpath;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Column(name="version_")
	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}
@Column(columnDefinition="varchar2(20)")
	public String getZhinajinbili() {
		return zhinajinbili;
	}

	public void setZhinajinbili(String zhinajinbili) {
		this.zhinajinbili = zhinajinbili;
	}
	@Column(columnDefinition="varchar2(20)")
	public String getWeiyuejinbili() {
		return weiyuejinbili;
	}

	public void setWeiyuejinbili(String weiyuejinbili) {
		this.weiyuejinbili = weiyuejinbili;
	}
	

	public String getIsOverDate() {
		return isOverDate;
	}

	public void setIsOverDate(String isOverDate) {
		this.isOverDate = isOverDate;
	}
	@Column(columnDefinition="number(1) default 0")
	public int getHksd() {
		return hksd;
	}

	public void setHksd(int hksd) {
		this.hksd = hksd;
	}
	

	
	
}
