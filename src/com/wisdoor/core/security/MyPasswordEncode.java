package com.wisdoor.core.security;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
 
public class MyPasswordEncode extends MessageDigestPasswordEncoder{

	private String encode;
	
	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public MyPasswordEncode(String encode){
		super(encode);
		this.encode = encode;
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass, salt);
        return pass1.equalsIgnoreCase(pass2);
	}

}