package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;


/**
 * 信息修改变更日志表 datatype member
 */
@Entity
@Table(name = "t_ModifyLog",schema="KT")  
public class ModifyLog implements Serializable{

	/**
	 * datatype member
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private User changer;//操作人
	private String datatype;//数据类型 会员member、融资项目fb等
	private String dataname;//数据名称 英语简称用于区分 暂时使用方法名
	private Date modifyDate;//修改时间
	private String modifyIP;//修改者IP
	private String content;//修改内容 汉语内容用于显示
	private String modifyModel;//被修改的实体类，如果是会员则记录member的id
	private String firstData;//修改前的数据
	private String endData;//修改后的数据
	private String note;//备注 为手工添加或者修改的时候添加
	
	public ModifyLog() {
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
	
	@OneToOne
	public User getChanger() {
		return changer;
	}
	public void setChanger(User changer) {
		this.changer = changer;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyIP() {
		return modifyIP;
	}
	public void setModifyIP(String modifyIP) {
		this.modifyIP = modifyIP;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFirstData() {
		return firstData;
	}
	public void setFirstData(String firstData) {
		this.firstData = firstData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getDataname() {
		return dataname;
	}
	public void setDataname(String dataname) {
		this.dataname = dataname;
	}
	public String getModifyModel() {
		return modifyModel;
	}
	public void setModifyModel(String modifyModel) {
		this.modifyModel = modifyModel;
	}
}
