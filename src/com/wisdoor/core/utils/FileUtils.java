package com.wisdoor.core.utils;

import java.io.File;

public class FileUtils {
	// 获得文件的扩展名
	public static String getExtName(String fileName)
	{
		if (-1 == fileName.lastIndexOf("."))
		{
			return "";
		}

		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName
				.length());
	}
	
	// 删除指定目录下的所有文件，并同时删除该目录
	public static void deleteAll(File file)
	{
		if(!file.exists())
		{
			return;
		}
		
		if(!file.delete()) //目录中有文件
		{
			File[] subFiles = file.listFiles();
			
			for(int i = 0 ; i < subFiles.length; ++i)
			{
				if(subFiles[i].isDirectory())
				{
					deleteAll(subFiles[i]);
				}
				else
				{
					subFiles[i].delete();
				}
			}
			
			file.delete(); //删除该目录本身
		}
	}
	
	// 删除所有文件
	public static void deleteFile(File file)
	{
		if(file.isFile() && file.exists()) {
			file.delete(); 
		}
	}
}
