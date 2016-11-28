package com.kmfex.webservice.client;
  

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.webservice.client.sms.Chargeinterface;
import com.kmfex.webservice.client.sms.ChargeinterfacePortType;

@Service
@Transactional
public class WsInterfaceServiceImp implements WsInterfaceService {  
	private ChargeinterfacePortType port; 

	@Override
	public ChargeinterfacePortType getChargeinterfacePort() {
		Chargeinterface server = new Chargeinterface();
		port = server.getChargeinterfaceHttpPort();
		return port;
	}

}
