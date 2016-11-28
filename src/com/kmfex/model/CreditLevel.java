package com.kmfex.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * 信用等级
 * @author yhw   
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Credit_Level",schema="KT")
public class CreditLevel implements Serializable {
	private String id;
	private String grade;    //等级
	private String meaning;  //含义
	private String memo;     //说明
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public String getGrade() {
		return grade;
	}
	public String getMeaning() {
		return meaning;
	}
	public String getMemo() {
		return memo;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	
}
