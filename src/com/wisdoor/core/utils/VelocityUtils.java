package com.wisdoor.core.utils;
 

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class VelocityUtils {
	public static  String   getVelocityString(VelocityContext context,String velocityUrl) { 
		StringWriter writer =null;
		try {  
		    writer = new StringWriter();  
		    Template template = Velocity.getTemplate(velocityUrl); 
		    template.merge(context, writer);   
		    return writer.toString(); 
		} catch (Exception e) { 
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}     
		
	   return null;
	}
}
