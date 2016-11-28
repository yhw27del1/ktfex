/**
 * 
 */
package com.kmfex.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


/**
 * @author lanwd
 *
 */
public class FileIOHelper {
	private static Logger logger = Logger.getLogger(HttpclientHelper.class);
	public static String inputStreamTOString(InputStream is){
		return inputStreamTOString(is,"utf-8");
    }
	
	public static String inputStreamTOString(InputStream is,String charSet){
		 StringBuffer buffer = new StringBuffer();
	        try {
	        	BufferedReader in = new BufferedReader(new InputStreamReader(is,charSet));
	            String line = "";
				while ((line = in.readLine()) != null){
				  buffer.append(line);
				}
			} catch (IOException e) {
				logger.info(e.toString());
				e.printStackTrace();
			}
	        return buffer.toString();
	}

}
