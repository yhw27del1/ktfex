package com.wisdoor.core.remotefile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileServiceServlet extends HttpServlet
{
	private static final long serialVersionUID = -6995010688944830504L; 
	// 上传图片的大小
	protected long MAX_SIZE = 1000000; 
	public void init() throws ServletException {

		//判断是否设置了上传图片的大小
		if(this.getInitParameter("MAX_SIZE") != null){
			MAX_SIZE = Integer.parseInt(this.getInitParameter("MAX_SIZE"));
		}
	}
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String savePath ="";
		//判断是否设置了上传图片的大小
		if(this.getInitParameter("MAX_SIZE") != null){
			MAX_SIZE = Integer.parseInt(this.getInitParameter("MAX_SIZE"));
		}  
		
		// 准备上传图片
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List<?> items = null; 
		try {
			items = upload.parseRequest(request);			
			Iterator<?> itr = items.iterator();
			Iterator<?> itrF = items.iterator();
			
			while (itr.hasNext()) { 
				  FileItem item = (FileItem) itr.next(); 
				  if(item.isFormField()){ 
	                   String strname = item.getFieldName(); 
	                   if("filePath".equals(strname)){
	                	   savePath=this.getServletContext().getRealPath(item.getString());
	                	   //savePath=item.getString();
	                   }
	               }
			} 
			
			if (savePath == null || savePath.isEmpty()) {
				savePath=this.getServletContext().getRealPath("/Static/userfiles"); 
			} 
			
			// 检查目录
			File uploadDir = new File(savePath);
		 
			// 如果没有文件夹，则创建
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			
			
			while (itrF.hasNext()) { 
				FileItem itemF = (FileItem) itrF.next();
				 // 上传图片的原文件名   
		        String fileName = itemF.getName();   
				if (!itemF.isFormField()) {  
			 	   File uploadedFile = new File(savePath, fileName);						
			 	   itemF.write(uploadedFile); 
				}
			} 
			
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.flush();
			out.close(); 
		}

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
 

	public long getMAX_SIZE() {
		return MAX_SIZE;
	}

	public void setMAX_SIZE(long max_size) {
		MAX_SIZE = max_size;
	}
 
	
}