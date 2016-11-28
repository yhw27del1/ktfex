package com.wisdoor.core.utils.kindeditor.upload; 
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现KindEditor图片上传的Servlet
 * 
 * @author  
 * 
 * @since 2012/03/21 20:20:23
 */
public class UploadImageManager extends HttpServlet {

	private static final long serialVersionUID = -8359652838938248988L;
	// 定义允许上传的图片的扩展名
	protected String[] FILETYPES = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };
	// 定义上传图片保存目录路径
	protected String UPLOAD_PATH = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String savePath = this.getInitParameter("UPLOAD_PATH");
		if (savePath == null || savePath.isEmpty()) {
			out.println(alertMsg("你还没设置读取上传图片保存的目录路径!"));
			return;
		}
		// 图片保存目录路径
		String rootPath = new StringBuffer(request.getSession().getServletContext().getRealPath("/")).append(savePath).toString();		
		// 图片保存目录URL
		String rootUrl = new StringBuffer(request.getContextPath()).append(savePath).toString();
		//根据path参数，设置各路径和URL
		String path = request.getParameter("path") != null ? request.getParameter("path") : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}

		//排序形式，name or size or type
		String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

		//不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			out.println(alertMsg("不允许使用移动到上一级目录"));
			return;
		}
		
		//最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			out.println("Parameter is not valid.");
			return;
		}
		//目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if(!currentPathFile.isDirectory()){
			out.println("Directory does not exist.");
			return;
		}

		//遍历目录取的文件信息
		List<Hashtable<?,?>> fileList = new ArrayList<Hashtable<?,?>>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(FILETYPES).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}

		if ("size".equals(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}
		
		out.println(toJSONString(currentUrl, currentDirPath, moveupDirPath, fileList));
		
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * 输出打印上传失败语句的脚本
	 * 
	 * @param message    失败信息
	 * 
	 * @return 页面打印的脚本语句
	 */
	public String alertMsg(String message) {
		StringBuffer sb = new StringBuffer("<script type\"text/javascript\">");
		sb.append("alert(\"").append(message).append("\");");
		sb.append("</script>");
		return sb.toString();
	}
	
	public String toJSONString(String currentUrl, String currentDirPath, String moveupDirPath, List<Hashtable<?, ?>> fileList){
		StringBuilder sb = new StringBuilder("{\"current_url\":\"");
		sb.append(currentUrl).append("\",").append("\"current_dir_path\":\"");
		sb.append(currentDirPath).append("\",\"moveup_dir_path\":\"").append(moveupDirPath).append("\",");
		sb.append("\"file_list\":[");
		int i = 0;
		sb.append("{");
		for(Hashtable<?,?> he : fileList){	
			if(i != (fileList.size() - 1)){
				sb.append("\"filename\":\"").append(he.get("filename")).append("\",");
				sb.append("\"filesize\":").append(he.get("filesize")).append(",");
				sb.append("\"filetype\":\"").append(he.get("filetype")).append("\",");
				sb.append("\"has_file\":").append(he.get("has_file")).append(",");
				sb.append("\"is_dir\":").append(he.get("is_dir")).append(",");
				sb.append("\"is_photo\":").append(he.get("is_photo")).append(",");
				sb.append("\"datetime\":\"").append(he.get("datetime")).append("\"");
				sb.append("},{");
			}else{
				sb.append("\"filename\":\"").append(he.get("filename")).append("\",");
				sb.append("\"filesize\":").append(he.get("filesize")).append(",");
				sb.append("\"filetype\":\"").append(he.get("filetype")).append("\",");
				sb.append("\"has_file\":").append(he.get("has_file")).append(",");
				sb.append("\"is_dir\":").append(he.get("is_dir")).append(",");
				sb.append("\"is_photo\":").append(he.get("is_photo")).append(",");
				sb.append("\"datetime\":\"").append(he.get("datetime")).append("\"");
				sb.append("}");
			}
			i++;
		}
		i = 0;
		sb.append("],\"total_count\":").append(fileList.size()).append("}");
		return sb.toString();
	}

	public class NameComparator implements Comparator<Object> {
		public int compare(Object a, Object b) {
			Hashtable<?, ?> hashA = (Hashtable<?, ?>) a;
			Hashtable<?, ?> hashB = (Hashtable<?, ?>) b;
			if (((Boolean) hashA.get("is_dir"))
					&& !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir"))
					&& ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String) hashA.get("filename")).compareTo((String) hashB.get("filename"));
			}
		}
	}

	public class SizeComparator implements Comparator<Object> {
		public int compare(Object a, Object b) {
			Hashtable<?, ?> hashA = (Hashtable<?, ?>) a;
			Hashtable<?, ?> hashB = (Hashtable<?, ?>) b;
			if (((Boolean) hashA.get("is_dir"))
					&& !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir"))
					&& ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				if (((Long) hashA.get("filesize")) > ((Long) hashB.get("filesize"))) {
					return 1;
				} else if (((Long) hashA.get("filesize")) < ((Long) hashB.get("filesize"))) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	public class TypeComparator implements Comparator<Object> {
		public int compare(Object a, Object b) {
			Hashtable<?, ?> hashA = (Hashtable<?, ?>) a;
			Hashtable<?, ?> hashB = (Hashtable<?, ?>) b;
			if (((Boolean) hashA.get("is_dir"))
					&& !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir"))
					&& ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
			}
		}
	}

}