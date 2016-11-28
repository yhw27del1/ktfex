package com.kmfex.webservice.client.hx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for FailedLiquidationEntry complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="FailedLiquidationEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="amt" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="flag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="merAccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FailedLiquidationEntry", namespace = "http://entry.response.B2B.hitrust.com", propOrder = {
		"accountNo", "amt", "flag", "merAccountNo", "remark", "type" })
public class FailedLiquidationEntry {

	@XmlElementRef(name = "accountNo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> accountNo;
	protected Double amt;
	@XmlElementRef(name = "flag", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> flag;
	@XmlElementRef(name = "merAccountNo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> merAccountNo;
	@XmlElementRef(name = "remark", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> remark;
	@XmlElementRef(name = "type", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> type;

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
	 * Gets the value of the amt property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getAmt() {
		return amt;
	}

	/**
	 * Sets the value of the amt property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setAmt(Double value) {
		this.amt = value;
	}

	/**
	 * Gets the value of the flag property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getFlag() {
		return flag;
	}

	/**
	 * Sets the value of the flag property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setFlag(JAXBElement<String> value) {
		this.flag = ((JAXBElement<String>) value);
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

	/**
	 * Gets the value of the remark property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getRemark() {
		return remark;
	}

	/**
	 * Sets the value of the remark property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setRemark(JAXBElement<String> value) {
		this.remark = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setType(JAXBElement<String> value) {
		this.type = ((JAXBElement<String>) value);
	}

}
