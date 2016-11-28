package com.kmfex.webservice.client.hx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for InOutDetailCheckEntry complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="InOutDetailCheckEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="amt" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="bankTxSerNoHis" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="merAccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="merTxSerNoHis" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trnxCodeHis" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trnxType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InOutDetailCheckEntry", namespace = "http://entry.response.B2B.hitrust.com", propOrder = {
		"accountName", "accountNo", "amt", "bankTxSerNoHis", "merAccountNo",
		"merTxSerNoHis", "trnxCodeHis", "trnxType" })
public class InOutDetailCheckEntry {

	@XmlElementRef(name = "accountName", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> accountName;
	@XmlElementRef(name = "accountNo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> accountNo;
	protected Double amt;
	@XmlElementRef(name = "bankTxSerNoHis", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> bankTxSerNoHis;
	@XmlElementRef(name = "merAccountNo", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> merAccountNo;
	@XmlElementRef(name = "merTxSerNoHis", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> merTxSerNoHis;
	@XmlElementRef(name = "trnxCodeHis", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> trnxCodeHis;
	@XmlElementRef(name = "trnxType", namespace = "http://entry.response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> trnxType;

	/**
	 * Gets the value of the accountName property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getAccountName() {
		return accountName;
	}

	/**
	 * Sets the value of the accountName property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setAccountName(JAXBElement<String> value) {
		this.accountName = ((JAXBElement<String>) value);
	}

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
	 * Gets the value of the bankTxSerNoHis property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getBankTxSerNoHis() {
		return bankTxSerNoHis;
	}

	/**
	 * Sets the value of the bankTxSerNoHis property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setBankTxSerNoHis(JAXBElement<String> value) {
		this.bankTxSerNoHis = ((JAXBElement<String>) value);
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
	 * Gets the value of the merTxSerNoHis property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getMerTxSerNoHis() {
		return merTxSerNoHis;
	}

	/**
	 * Sets the value of the merTxSerNoHis property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setMerTxSerNoHis(JAXBElement<String> value) {
		this.merTxSerNoHis = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the trnxCodeHis property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getTrnxCodeHis() {
		return trnxCodeHis;
	}

	/**
	 * Sets the value of the trnxCodeHis property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setTrnxCodeHis(JAXBElement<String> value) {
		this.trnxCodeHis = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the trnxType property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getTrnxType() {
		return trnxType;
	}

	/**
	 * Sets the value of the trnxType property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setTrnxType(JAXBElement<String> value) {
		this.trnxType = ((JAXBElement<String>) value);
	}

}
