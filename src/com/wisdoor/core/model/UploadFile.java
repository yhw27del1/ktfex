package com.wisdoor.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * 文件管理中心
 * @author   
 */
@Entity
@Table(name="sys_UploadFile",schema="KT")
public class UploadFile implements Serializable{
	
	private static final long serialVersionUID = 7291850391235809042L;
	
	private String id;
	//相对路径
	private String filepath;
    //绝对路径
	private String path;
	//上传时间
	private Date uploadtime = new Date();
	// 扩展名
	private String ext;
	// 文件大小
	private Long filesize; 
	// 附件类型（图片、文件、视频）
	private String filetype;
	//上传时文件名
	private String fileName; 
	//文件使用状态(0未使用定时删除，1使用中)
	private String useFlag="0"; 
	// 备注
	private String rank;
	// 附件分类
	private String keyword;
	// 创建人编号
	private User user;  
	// 来自的实体model对象(比如entityFrom="FinancingBase" 融资项目基本信息表上传的附件)
	private String entityFrom;  
	// 来自的实体那个model对象记录ID(比如entityId="融资项目基本信息表ID")
	private String entityId;
	
	//辅助变量
	private String frontId;
	public UploadFile() {} 
	
	public UploadFile(String filepath) {
		this.filepath = filepath;
	}  
	
	
	public UploadFile(String id, String useFlag, String entityFrom,
			String entityId) { 
		this.id = id;
		this.useFlag = useFlag;
		this.entityFrom = entityFrom;
		this.entityId = entityId;
	}

 

	public UploadFile(String id, String useFlag, String fileName, String rank,
			String entityFrom, String entityId) { 
		this.id = id;
		this.fileName = fileName;
		this.useFlag = useFlag;
		this.rank = rank;
		this.entityFrom = entityFrom;
		this.entityId = entityId;
	}

	@Id 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	} 
	
	
 
	@Column(name="filepath_",nullable=false,length=80)
	public String getFilepath() {
		return filepath;
	}
	@Transient
	public String getFrontId() {
		return frontId;
	}

	public void setFrontId(String frontId) {
		this.frontId = frontId;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="uploadtime_")
	public Date getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}
	
	@Column(name="ext_")
	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	@Column(name="filesize_")
	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}
	@Column(name="filetype_")
	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	@Column(name="keyword_")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@Column(name="rank_")
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	} 
	@Column(name="path_")
	public String getPath() {
		return path;
	}
	@ManyToOne
	@JoinColumn(name="user_id_")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@Column(name="fileName_")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setPath(String path) {
		this.path = path;
	} 

	public String getEntityFrom() {
		return entityFrom;
	}

	public void setEntityFrom(String entityFrom) {
		this.entityFrom = entityFrom;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
 
	public String getUseFlag() {
		return useFlag;
	}

	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UploadFile other = (UploadFile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
