package com.kmfex.webservice.client.hx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for FailedAccountCheckEntry complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="FailedAccountCheckEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="amt" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="amtUse" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="bankAmt" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="bankAmtUse" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
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
@XmlType(name = "FailedAccountCheckEntry", namespace = "http://entry.response.B2B.hitrust.com", propOrder = {
		"accountNo", "amt", "amtUse", "bankAmt", "bankAmtUse", "merAccountNo" })
public class FailedAccountCheckEntry {

	@XmlElementRef(name = "accountNo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> accountNo;
	protected Double amt;
	protected Double amtUse;
	protected Double bankAmt;
	protected Double bankAmtUse;
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
	 * Gets the value of the amtUse property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getAmtUse() {
		return amtUse;
	}

	/**
	 * Sets the value of the amtUse property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setAmtUse(Double value) {
		this.amtUse = value;
	}

	/**
	 * Gets the value of the bankAmt property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getBankAmt() {
		return bankAmt;
	}

	/**
	 * Sets the value of the bankAmt property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setBankAmt(Double value) {
		this.bankAmt = value;
	}

	/**
	 * Gets the value of the bankAmtUse property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getBankAmtUse() {
		return bankAmtUse;
	}

	/**
	 * Sets the value of the bankAmtUse property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setBankAmtUse(Double value) {
		this.bankAmtUse = value;
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
