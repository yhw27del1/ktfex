package com.kmfex.webservice.client.hx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ReAccountRegEntry complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ReAccountRegEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dealerOperNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="merAccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReAccountRegEntry", namespace = "http://entry.response.B2B.hitrust.com", propOrder = {
		"accountNo", "dealerOperNo", "errorInfo", "merAccountNo" })
public class ReAccountRegEntry {

	@XmlElementRef(name = "accountNo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> accountNo;
	@XmlElementRef(name = "dealerOperNo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> dealerOperNo;
	@XmlElementRef(name = "errorInfo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> errorInfo;
	@XmlElementRef(name = "merAccountNo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> merAccountNo;

	/**
	 * Gets the value of the accountNo property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getAccountNo() {
		return accountNo;
	}

	/**
	 * Sets the value of the accountNo property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setAccountNo(JAXBElement<String> value) {
		this.accountNo = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the dealerOperNo property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getDealerOperNo() {
		return dealerOperNo;
	}

	/**
	 * Sets the value of the dealerOperNo property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setDealerOperNo(JAXBElement<String> value) {
		this.dealerOperNo = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the errorInfo property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getErrorInfo() {
		return errorInfo;
	}

	/**
	 * Sets the value of the errorInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setErrorInfo(JAXBElement<String> value) {
		this.errorInfo = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the merAccountNo property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getMerAccountNo() {
		return merAccountNo;
	}

	/**
	 * Sets the value of the merAccountNo property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setMerAccountNo(JAXBElement<String> value) {
		this.merAccountNo = ((JAXBElement<String>) value);
	}

}
