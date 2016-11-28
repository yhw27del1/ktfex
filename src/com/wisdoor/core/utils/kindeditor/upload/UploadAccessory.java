package com.wisdoor.core.utils.kindeditor.upload;
 
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 实现KindEditor图片上传的Servlet
 * 
 * @author  
 * 
 * @since 2012/03/21 20:20:23
 */
public class UploadAccessory extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// 上传文件的大小
	protected long MAX_SIZE = 1000000;
	// 定义允许上传的文件的扩展名
	protected String[] FILETYPES = new String[]{"doc","docx","xlsx","pptx","xls", "ppt", "pdf", "txt", "rar" , "zip"};
	// 定义上传文件保存目录路径
	protected String UPLOAD_PATH = "";
	
	protected String id = "";  

	protected String attachTitle = ""; 

	protected boolean isFlag = false;

	protected String tempTitle = "";


	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String savePath = this.getInitParameter("UPLOAD_PATH");
		if (savePath == null || savePath.isEmpty()) {
			out.println(alertMsg("你还没设置上传文件保存的目录路径!"));
			return;
		}
		//判断是否设置了上传文件的大小
		if(this.getInitParameter("MAX_SIZE") != null){
			MAX_SIZE = Integer.parseInt(this.getInitParameter("MAX_SIZE"));
		}
		//判断是否设置了上传文件的类型
		if(this.getInitParameter("FILETYPES") != null){
			FILETYPES = toArray(this.getInitParameter("FILETYPES"));
		}
		// 文件保存目录路径
		String uploadPath = new StringBuffer(request.getSession().getServletContext().getRealPath("/")).append(savePath).toString();

		// 文件保存目录URL
		String saveUrl = new StringBuffer(request.getContextPath()).append(savePath).toString();

		if(!ServletFileUpload.isMultipartContent(request)){	
			out.println(alertMsg("请选择要上传的文件。"));
			return;
		}
		//检查目录
		File uploadDir = new File(uploadPath);
		if(!uploadDir.isDirectory()){
			out.println(alertMsg("上传目录不存在。"));
			return;
		}
		//检查目录写权限
		if(!uploadDir.canWrite()){
			out.println(alertMsg("当前角色对上传目录没有写权限。"));
			return;
		}

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		String temp = null;
		String ext = null;
		try{
			List<?> items = upload.parseRequest(request);
			Iterator<?> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				String fileName = item.getName();
				temp = (String) item.getName();
				if(temp != null && !isFlag){
					temp = temp.substring(temp.lastIndexOf("\\")+1);
					tempTitle = temp;
					isFlag = true;
				}
				// KindEditor编辑器的ID  
				if(((String)item.getFieldName()).equals("id")){   
					id = item.getString(); 
				}  
				// 上传图片的重新提示
				if(((String)item.getFieldName()).equals("attachTitle")){   
					attachTitle = item.getString(); 
					if(attachTitle != null){
						attachTitle = new String(attachTitle.getBytes("ISO8859-1"),"UTF-8");
					}
				} 
				if (!item.isFormField()) {
					//检查文件大小
					if(item.getSize() > MAX_SIZE){						
						out.println(alertMsg("上传文件大小超过限制。"));
						return;
					}
					//检查扩展名
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					if(!Arrays.<String>asList(FILETYPES).contains(fileExt)){
						out.println(alertMsg("上传文件扩展名是不允许的扩展名。"));
						return;
					}
					// 根据时间创建文件夹
					SimpleDateFormat folderNameFormat = new SimpleDateFormat("yyyyMMdd");
					String realPath = uploadPath + folderNameFormat.format(new Date());
					File folder = new File(realPath);
					boolean flag = folder.exists();
					// 确认文件夹是否已经存在
					if(!flag){
						flag = folder.mkdir();
					}
					// 创建文件夹并上传图片
					if(flag){
						SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMddHHmmss");					
						String newFileName = fileNameFormat.format(new Date()) + "_"+ new Random().nextInt(1000) + "." + fileExt;						
						File uploadedFile = new File(realPath, newFileName);						
						item.write(uploadedFile);
						saveUrl += folderNameFormat.format(new Date()) + "/" + newFileName;	
						ext = fileExt;
					}else{
						System.out.println(" 文件夹创建失败，请确认磁盘没有写保护并且空件足够");
					}				
				}
			}
			

			if(attachTitle == null || attachTitle.isEmpty()){
				attachTitle = tempTitle;
			}
			
			out.println(insertAttach(id, saveUrl, attachTitle, ext));
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
			isFlag = false;
		}
	}

	
	/**
	 * 输出打印上传失败语句的脚本
	 * 
	 * @param message    失败信息
	 * 
	 * @return 页面打印的脚本语句
	 */
	public String alertMsg(String message){
		
		StringBuilder sb = new StringBuilder("<html>");
		sb.append("<head>").append("<title>error</title>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<script type=\"text/javascript\">");
		sb.append("alert(\"").append(message).append("\");history.back();</script>");
		sb.append("</body>").append("</html>");

		return sb.toString();
	}
	
	/**
	 * 输出插入附件至编辑器语句的脚本
	 * 
	 * @param id     编辑器ID
	 * 
	 * @param url    上传附件的地址
	 * 
	 * @param title  上传时设置的title属性
	 * 
	 * @param ext    上传文件的后缀名
	 * 
	 * @return  插入附件至编辑器的脚本语句
	 */
	public String insertAttach(String id, String url, String title, String ext){
		StringBuilder sb = new StringBuilder("<html>");
		sb.append("<head>").append("<title>Insert Accessory</title>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<script type=\"text/javascript\">");
		sb.append("parent.KE.plugin[\"accessory\"].insert(\"").append(id).append("\",\"");
		sb.append(url).append("\",\"").append(title).append("\",\"").append(ext).append("\");</script>");
		sb.append("</body>").append("</html>");
		return sb.toString();
	}

	/**
	 * 输出允许上传图片类型的数组
	 * 
	 * @param filesType 允许上传的图片类型
	 * 
	 * @return 允许上传图片类型
	 */
	public String[] toArray(String filesType){

		if(filesType == null){
			return null;
		}

		String[] types = filesType.split(",");
		String[] allowTypes = new String[types.length];
		int i = 0;
		for(String type : types){
			allowTypes[i] = type;
			i++;
		}

		return allowTypes;
	}
}