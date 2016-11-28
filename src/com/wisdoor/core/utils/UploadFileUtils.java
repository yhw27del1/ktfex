package com.wisdoor.core.utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.servlet.ServletContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import com.wisdoor.core.model.UploadFile;
 
/**
 * 文件上传帮助类
 * @author  
 *
 */
public class UploadFileUtils {
		private static final String configFile="RemoteFileconfig.properties";
		
		private final Properties prop=new Properties(); 
		 
		
	    private static String allowExtType;
		
	    private static long maxSize=204800; //字节 
	    
	    private static String uploadDir="/Static/userfiles";
	    
	    /**
	     * 容器上下文
	     */
	    private ServletContext context;
	    
	    private String uploadDirPath="";
	    
	    public UploadFileUtils(ServletContext context)
	    {
	      this.context=context;	
	    }
	    
	    /**
	     * 保存文件信息
	     * @param file
	     * @param fileName
	     * @param fileType
	     * @return
	     * @throws Exception
	     */
		public UploadFile saveFile(File file,String fileName,String fileType) throws Exception {
			try {
				initUploadDir();
				String ext="";
					ext = getExtension(fileName);
					if(file.length() <= 0){   
	                    throw new IllegalArgumentException("上传的文件:"+fileName+",不能为空！");   
	                } 
					//是否超过最大值
					if(maxSize>0&&file.length()>maxSize)
						throw new IllegalArgumentException("上传的文件:"+fileName+",大小超过了最大值:"+maxSize/1024+"KB");
					
					//是否是容许类型
					if(!extIsAllowed(ext))
						throw new IllegalArgumentException("上传的文件:"+fileName+"，文件类型不合法!");
					//生成的唯一键   
					UploadFile uploadFile=new UploadFile();
				    
					SimpleDateFormat format =new SimpleDateFormat("yyyyMMddHHmmss");
					String id=format.format(new Date()); 
					Random ra=new Random();
					   for(int j=0;j<5;j++){
						   id+=ra.nextInt(10);
					   }
				   ext=ext.toLowerCase();
				   uploadFile.setId(id+"."+ext);
				   uploadFile.setPath(uploadDir+"/"+id+"."+ext);
				   uploadFile.setFilepath(uploadDir+"/"+id+"."+ext);
				   uploadFile.setFileName(fileName);
				   uploadFile.setFiletype(fileType);
				   uploadFile.setExt(ext);
				   uploadFile.setFilesize(file.length());
				   uploadFile.setPath(uploadDirPath+id+"."+ext); 
				    //加入登录者ID
					 
					file.renameTo(new File(uploadDirPath, id+"."+ext));  
					return uploadFile;
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("文件上传失败："+e.getMessage());
			}
		}
		
	    
	    /**
	     * 保存文件在远程服务器
	     * @param file
	     * @param fileName
	     * @param fileType
	     * @param ip 来自服务器Ip
	     * @return
	     * @throws Exception
	     */
		public UploadFile saveFile(File file,String fileName,String fileType,String ip) throws Exception {
			initProp();//初始化文件
			String URL_REMOTE_IP = prop.getProperty("URL_REMOTE_IP")!=null?prop.getProperty("URL_REMOTE_IP"):"localhost";//请求的IP  
			String URL_REMOTE_PORT = prop.getProperty("URL_REMOTE_PORT")!=null?prop.getProperty("URL_REMOTE_PORT"):"8080";//请求的端口
			String OPEN_REMOTE = prop.getProperty("OPEN_REMOTE")!=null?prop.getProperty("OPEN_REMOTE"):"false";//是否开启远程上传
			String REMOTE_FILE_PATH = prop.getProperty("REMOTE_FILE_PATH")!=null?prop.getProperty("REMOTE_FILE_PATH"):"/Static/userfiles";//远程服务地址
 			if("true".equals(OPEN_REMOTE)){//启用远程 
				UploadFile uploadFile=saveRemotTempFile(file,fileName,fileType);//先保存在临死文件里面
				PostMethod filePost = new PostMethod("http://"+URL_REMOTE_IP+":"+URL_REMOTE_PORT+"/upload/UploadRemoteFile.html");  
				try {   
					   if(null!=ip&&!"".equals(ip)){
						   uploadFile.setRank("来自ip为"+ip+"的服务器"); 
					   } 
					    File  newFile=new File(uploadDirPath,uploadFile.getId());
						Part[] parts = { new FilePart(uploadFile.getId(),newFile),new StringPart("filePath",REMOTE_FILE_PATH)};
						filePost.setRequestEntity(new MultipartRequestEntity(parts,filePost.getParams())); 
						HttpClient client = new HttpClient();
						client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
						int status = client.executeMethod(filePost);
						if (status == HttpStatus.SC_OK)
						{ 
							uploadFile.setFilepath("http://"+URL_REMOTE_IP+":"+URL_REMOTE_PORT+REMOTE_FILE_PATH+"/"+uploadFile.getId());
							newFile.delete(); 
							return uploadFile;
						}else{  
							return null;
						}  
				} catch (Exception e) {
					e.printStackTrace();
					throw new IllegalArgumentException("文件上传失败："+e.getMessage()); 
				} finally {
					filePost.releaseConnection();
				} 
 			}else{//不启用远程 
 				UploadFile uploadFile=saveFile(file,fileName,fileType);//先保存在临死文件里面
			    if(null!=ip&&!"".equals(ip)){
				   uploadFile.setRank("来自ip为"+ip+"的服务器"); 
			    } 
   				return uploadFile;
 			}

		}
			
	    /**
	     * 保存文件信息--先保存在临时文件
	     * @param file
	     * @param fileName
	     * @param fileType
	     * @return
	     * @throws Exception
	     */
		public UploadFile saveRemotTempFile(File file,String fileName,String fileType) throws Exception {
			try {
				uploadDir="/Static/userfiles/temp";
				initUploadDir();
				String ext="";
					ext = getExtension(fileName);
					if(file.length() <= 0){   
	                    throw new IllegalArgumentException("上传的文件:"+fileName+",不能为空！");   
	                } 
					//是否超过最大值
					if(maxSize>0&&file.length()>maxSize)
						throw new IllegalArgumentException("上传的文件:"+fileName+",大小超过了最大值:"+maxSize/1024+"KB");
					
					//是否是容许类型
					if(!extIsAllowed(ext))
						throw new IllegalArgumentException("上传的文件:"+fileName+"，文件类型不合法!");
					//生成的唯一键   
					UploadFile uploadFile=new UploadFile();
				    
					SimpleDateFormat format =new SimpleDateFormat("yyyyMMddHHmmss");
					String id=format.format(new Date()); 
					Random ra=new Random();
					   for(int j=0;j<5;j++){
						   id+=ra.nextInt(10);
					   }
				   ext=ext.toLowerCase();
				   uploadFile.setId(id+"."+ext);
				   uploadFile.setPath(uploadDir+"/"+id+"."+ext);
				   uploadFile.setFilepath(uploadDir+"/"+id+"."+ext);
				   uploadFile.setFileName(fileName);
				   uploadFile.setFiletype(fileType);
				   uploadFile.setExt(ext);
				   uploadFile.setFilesize(file.length());
				   uploadFile.setPath(uploadDirPath+id+"."+ext); 
				    //加入登录者ID
					 
					file.renameTo(new File(uploadDirPath, id+"."+ext));  
					return uploadFile;
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("文件上传失败："+e.getMessage());
			}
		}
		private void initProp() {
			InputStream in=UploadFileUtils.class.getResourceAsStream(configFile);
			if(in!=null){
				prop.clear();
				try {
					prop.load(in);
				} catch (IOException e) { 
					e.printStackTrace();
				}
			}
		} 
		/**
		 * 获得文件扩展名
		 * @param fileName
		 * @return
		 */
		private String getExtension(String fileName) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		}
	 
	    /**
		 * 获得文件上传目录
		 * @return
		 */
		private void initUploadDir() {
			if(uploadDirPath!=null&&uploadDirPath.length()>1)
				return ;
			// 如果为空
			if (uploadDir == null || uploadDir.equals(""))
				uploadDir = "/Static/userfiles";
			String dirPath =context.getRealPath(uploadDir);
			//添加文件夹结束信息
			if (uploadDir.startsWith("/")==false&& uploadDir.startsWith("\\")==false) {
				uploadDir= File.separator+uploadDir;
			}
			File dict = new File(dirPath);
			// 如果没有文件夹，则创建
			if (!dict.exists()) {
				dict.mkdir();
			}
			//添加文件夹结束信息
			if (dirPath.endsWith("/")==false&& dirPath.endsWith("\\")==false) {
				dirPath += File.separator;
			}
		    uploadDirPath=dirPath;
		}
		
		/**
		 * 上传的类型
		 * @param ext
		 * @return
		 */
		private boolean extIsAllowed(String ext) {
			ext=ext.toLowerCase();
			
			if(allowExtType==null || allowExtType.equals(""))
			    return true;
			
			for(String str:allowExtType.split(","))
			  if(str.equals(ext))
				  return true;
			
			return false;
		}
		
		/**
		 * 设置容许上传的文件类型
		 * @param allowExtType
		 */
		public static void setAllowExtType(String allowExtType) {
			UploadFileUtils.allowExtType = allowExtType;
		}

		public static void setMaxSize(long maxSize) {
			UploadFileUtils.maxSize = maxSize;
		}

		public static void setUploadDir(String uploadDir) {
			UploadFileUtils.uploadDir = uploadDir;
		}

		public void setContext(ServletContext context) {
			this.context = context;
		}

}
