
package com.kmfex.hxbank;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author linuxp
 *
 */
public class ConfigManager {
	private static final String configFile="load_bank.properties";
	
	private final Properties prop=new Properties();
	
	public final String HOST;
	public final int PORT;
	public final String URL;
	
	public ConfigManager(){
		initProp();
		
		HOST=prop.getProperty("host")!=null?prop.getProperty("host"):"116.55.243.30";
		PORT=prop.getProperty("port")!=null?Integer.parseInt(prop.getProperty("port")):443;
		URL=prop.getProperty("url")!=null?prop.getProperty("url"):"/bank/hxbServlet";
	}
 
	private void initProp() {
		InputStream in=ConfigManager.class.getResourceAsStream(configFile);
		if(in!=null){
			prop.clear();
			try {
				prop.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
