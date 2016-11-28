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
 * 收费项目
 * @author yhw   
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Cost_Base",schema="KT")
public class CostBase implements Serializable{

	private String id;
	
	private String name;
	private String code;
	private Date addtime = new Date();
	private int tariff = 0;//0：一次收费；1：按月收费
	private String memo;
	public CostBase(){}
	public CostBase(String name,String code){
		this.name = name;
		this.code = code;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getAddtime() {
		return addtime;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemo() {
		return memo;
	}
	public void setTariff(int tariff) {
		this.tariff = tariff;
	}
	@Column(columnDefinition="int default 0")
	public int getTariff() {
		return tariff;
	}
}
