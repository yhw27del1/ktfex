package com.kmfex.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;
/**
 * @author linuxp
 * */
@Entity
@Table(name = "t_opan_close",schema="KT")
public class OpenCloseDeal {
	private String id;
	private Date createDate = new Date();
	private String name;//交易名称，比如:开市，休市
	private User operator;//操作者
	private boolean success;//是否成功
	private String memo;
	public OpenCloseDeal(){
		
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	@ManyToOne
	public User getOperator() {
		return operator;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}
}
