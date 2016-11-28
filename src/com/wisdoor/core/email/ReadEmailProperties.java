package com.wisdoor.core.email;

import java.io.IOException;
import java.util.Properties;

public class ReadEmailProperties {
	private static Properties properties = new Properties();
	static{
		try {     
			properties.load(ReadEmailProperties.class.getClassLoader().getResourceAsStream("email.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readValue(String key){		
		return (String)properties.get(key);
	}
}
