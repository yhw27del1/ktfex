package com.kmfex.webservice.client.hx;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import com.kmfex.webservice.client.sms.Constant;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * <p>
 * An example of how this class may be used:
 * 
 * <pre>
 * hxbankinterface service = new hxbankinterface();
 * HxbankinterfacePortType portType = service.getHxbankinterfaceHttpPort();
 * portType.tradingMarketBankBalance(...);
 * </pre>
 * 
 * </p>
 * 
 */
@WebServiceClient(name = "hxbankinterface", targetNamespace = "http://webservice.kmfex.com", wsdlLocation = "http://"+Constant.WSDLURL+"/services/hxbankinterface?wsdl")
public class Hxbankinterface extends Service {

	private final static URL HXBANKINTERFACE_WSDL_LOCATION;
	private final static Logger logger = Logger
			.getLogger(com.kmfex.webservice.client.hx.Hxbankinterface.class
					.getName());

	static {
		URL url = null;
		try {
			URL baseUrl;
			baseUrl = com.kmfex.webservice.client.hx.Hxbankinterface.class
					.getResource(".");
			url = new URL(baseUrl,
					"http://"+Constant.WSDLURL+"/services/hxbankinterface?wsdl");
		} catch (MalformedURLException e) {
			logger
					.warning("Failed to create URL for the wsdl Location: 'http://"+Constant.WSDLURL+"/services/hxbankinterface?wsdl', retrying as a local file");
			logger.warning(e.getMessage());
		}
		HXBANKINTERFACE_WSDL_LOCATION = url;
	}

	public Hxbankinterface(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public Hxbankinterface() {
		super(HXBANKINTERFACE_WSDL_LOCATION, new QName(
				"http://webservice.kmfex.com", "hxbankinterface"));
	}

	/**
	 * 
	 * @return returns HxbankinterfacePortType
	 */
	@WebEndpoint(name = "hxbankinterfaceHttpPort")
	public HxbankinterfacePortType getHxbankinterfaceHttpPort() {
		return super.getPort(new QName("http://webservice.kmfex.com",
				"hxbankinterfaceHttpPort"), HxbankinterfacePortType.class);
	}

}
