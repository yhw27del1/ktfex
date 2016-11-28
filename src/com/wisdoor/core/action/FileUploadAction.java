package com.wisdoor.core.action;

 

import java.io.File;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit4.jfchart.mainClass;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wisdoor.core.model.UploadFile;
import com.wisdoor.core.service.UploadFileService;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.FileUtils;
import com.wisdoor.core.utils.IpAddrUtil;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.utils.UploadFileUtils;
import com.wisdoor.struts.BaseAction;
/*** 
 * 附件管理类
 * @author    
 */ 
@Controller
@Scope("prototype")
public class FileUploadAction  extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6913985265715642496L;
	@Resource UploadFileService uploadFileService; 
	private String fileId;
	private List<File> filedata; 
	private List<String> filedataFileName;
	private List<String> filedataContentType; 
	
	private final static String [] allowfiletype = new String[]{".jpg",".gif",".png",".bmp"};
	
	public String saveUploadFile() {
		try {
			//User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			UploadFile file = null;  
			boolean passed = false;
			if(StringUtils.isBlank(filedataFileName.get(0)) || filedataFileName.get(0).indexOf(".") == -1){
				return null;
			}
			String filename = filedataFileName.get(0);
			String ext = filename.substring(filename.lastIndexOf(".")).toLowerCase();
			for(String allowtype : allowfiletype){
				if(allowtype.equals(ext)){
					passed = true;
					break;
				}
			}
			if(!passed){
				throw new RuntimeException("上传非法文件");
			}
			if (filedata != null && filedata.size() > 0) { 
				UploadFileUtils fileUtils = new UploadFileUtils(
						ServletActionContext.getServletContext());
				file = fileUtils.saveFile(filedata.get(0), filedataFileName.get(0),filedataContentType.get(0),IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
				file.setUploadtime(new Date());
				//if(null!=loginUser)
				  // file.setUser(loginUser); 
				uploadFileService.insert(file); 
			}
			file.setUser(null);
			file.setFrontId(file.getId().replaceAll("\\.", ""));
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), file);
		} catch (Exception e) { 
			e.printStackTrace();
		}  
		return null;
	}
	public String delete(){ 
		try {
			
			UploadFile uploadFile=uploadFileService.selectById(fileId);
			FileUtils.deleteFile(new File(uploadFile.getPath()));
			
			uploadFileService.delete(fileId); 
			
			DoResultUtil.doStringResult(ServletActionContext.getResponse(), "1");
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return null;
	}
	public String saveUploadFiles() {
		try {
			//User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
 			UploadFile file = null; 
			List<Object> files=new ArrayList<Object>();
			boolean passed = false;
			if(StringUtils.isBlank(filedataFileName.get(0)) || filedataFileName.get(0).indexOf(".") == -1){
				return null;
			}
			String filename = filedataFileName.get(0);
			String ext = filename.substring(filename.lastIndexOf(".")).toLowerCase();
			for(String allowtype : allowfiletype){
				if(allowtype.equals(ext)){
					passed = true;
					break;
				}
			}
			if(!passed){
				throw new RuntimeException("上传非法文件");
			}
			if (filedata != null && filedata.size() > 0) { 
				UploadFileUtils fileUtils = new UploadFileUtils(
						ServletActionContext.getServletContext());
				file = fileUtils.saveFile(filedata.get(0), filedataFileName.get(0),filedataContentType.get(0),IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
				file.setUploadtime(new Date());
				//if(null!=loginUser)
					  // file.setUser(loginUser);
				uploadFileService.insert(file); 
				file.setUser(null); 
				file.setFrontId(file.getId().replaceAll("\\.", ""));
				files.add(file);
			}
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), files);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}

	public List<File> getFiledata() {
		return filedata;
	}

	public void setFiledata(List<File> filedata) {
		this.filedata = filedata;
	}

	public List<String> getFiledataFileName() {
		return filedataFileName;
	}

	public void setFiledataFileName(List<String> filedataFileName) {
		this.filedataFileName = filedataFileName;
	}

	public List<String> getFiledataContentType() {
		return filedataContentType;
	}

	public void setFiledataContentType(List<String> filedataContentType) {
		this.filedataContentType = filedataContentType;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	
}
