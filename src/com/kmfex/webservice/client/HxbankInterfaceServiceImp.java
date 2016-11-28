package com.kmfex.webservice.client;
  

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.webservice.client.hx.Hxbankinterface;
import com.kmfex.webservice.client.hx.HxbankinterfacePortType;

@Service
@Transactional
public class HxbankInterfaceServiceImp implements HxbankInterfaceService {  
	private HxbankinterfacePortType port; 

	@Override
	public HxbankinterfacePortType getChargeinterfacePort() {
		Hxbankinterface server = new Hxbankinterface();
		port = server.getHxbankinterfaceHttpPort();
		return port;
	}

}
