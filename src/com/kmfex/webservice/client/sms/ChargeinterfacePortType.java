package com.kmfex.webservice.client.sms;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "chargeinterfacePortType", targetNamespace = "http://webservice.kmfex.com")
public interface ChargeinterfacePortType {

	/**
	 * 
	 * @param content
	 * @param phone   
	 * @param sendtime
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "out", targetNamespace = "http://webservice.kmfex.com")
	@RequestWrapper(localName = "sendsms1", targetNamespace = "http://webservice.kmfex.com", className = "com.kmfex.webservice.client.sms.Sendsms1")
	@ResponseWrapper(localName = "sendsms1Response", targetNamespace = "http://webservice.kmfex.com", className = "com.kmfex.webservice.client.sms.Sendsms1Response")
	public String sendsms1(
			@WebParam(name = "phone", targetNamespace = "http://webservice.kmfex.com") String phone,
			@WebParam(name = "content", targetNamespace = "http://webservice.kmfex.com") String content,
			@WebParam(name = "sendtime", targetNamespace = "http://webservice.kmfex.com") String sendtime);

	/**
	 * 
	 * @param content
	 * @param phone
	 * @param sendtime
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "out", targetNamespace = "http://webservice.kmfex.com")
	@RequestWrapper(localName = "sendsms", targetNamespace = "http://webservice.kmfex.com", className = "com.kmfex.webservice.client.sms.Sendsms")
	@ResponseWrapper(localName = "sendsmsResponse", targetNamespace = "http://webservice.kmfex.com", className = "com.kmfex.webservice.client.sms.SendsmsResponse")
	public String sendsms(
			@WebParam(name = "phone", targetNamespace = "http://webservice.kmfex.com") ArrayOfString phone,
			@WebParam(name = "content", targetNamespace = "http://webservice.kmfex.com") String content,
			@WebParam(name = "sendtime", targetNamespace = "http://webservice.kmfex.com") String sendtime);

	/**
	 * 
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "out", targetNamespace = "http://webservice.kmfex.com")
	@RequestWrapper(localName = "smsrevice", targetNamespace = "http://webservice.kmfex.com", className = "com.kmfex.webservice.client.sms.Smsrevice")
	@ResponseWrapper(localName = "smsreviceResponse", targetNamespace = "http://webservice.kmfex.com", className = "com.kmfex.webservice.client.sms.SmsreviceResponse")
	public String smsrevice();

	/**
	 * 
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "out", targetNamespace = "http://webservice.kmfex.com")
	@RequestWrapper(localName = "getbalance", targetNamespace = "http://webservice.kmfex.com", className = "com.kmfex.webservice.client.sms.Getbalance")
	@ResponseWrapper(localName = "getbalanceResponse", targetNamespace = "http://webservice.kmfex.com", className = "com.kmfex.webservice.client.sms.GetbalanceResponse")
	public String getbalance();

}