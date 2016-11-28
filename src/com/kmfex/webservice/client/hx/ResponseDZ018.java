package com.kmfex.webservice.client.hx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ResponseDZ018 complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseDZ018">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="amtIn" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="amtOut" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="bankTxSerNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countIn" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="countOut" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="document" type="{http://util.B2B.hitrust.com}XMLDocument" minOccurs="0"/>
 *         &lt;element name="inoutDetails" type="{http://entry.response.B2B.hitrust.com}ArrayOfInOutDetailCheckEntry" minOccurs="0"/>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="merchantTrnxNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serverTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signature_Algorithm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="successed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="trnxCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseDZ018", namespace = "http://response.B2B.hitrust.com", propOrder = {
		"amtIn", "amtOut", "bankTxSerNo", "code", "countIn", "countOut",
		"document", "inoutDetails", "language", "merchantTrnxNo", "message",
		"serverTime", "signFlag", "signature", "signatureAlgorithm",
		"successed", "trnxCode", "version" })
public class ResponseDZ018 {

	protected Double amtIn;
	protected Double amtOut;
	@XmlElementRef(name = "bankTxSerNo", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> bankTxSerNo;
	@XmlElementRef(name = "code", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> code;
	protected Integer countIn;
	protected Integer countOut;
	@XmlElementRef(name = "document", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<XMLDocument> document;
	@XmlElementRef(name = "inoutDetails", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<ArrayOfInOutDetailCheckEntry> inoutDetails;
	@XmlElementRef(name = "language", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> language;
	@XmlElementRef(name = "merchantTrnxNo", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> merchantTrnxNo;
	@XmlElementRef(name = "message", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> message;
	@XmlElementRef(name = "serverTime", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> serverTime;
	@XmlElementRef(name = "signFlag", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> signFlag;
	@XmlElementRef(name = "signature", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> signature;
	@XmlElementRef(name = "signature_Algorithm", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> signatureAlgorithm;
	protected Boolean successed;
	@XmlElementRef(name = "trnxCode", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> trnxCode;
	@XmlElementRef(name = "version", namespace = "http://response.B2B.hitrust.com", type = JAXBElement.class)
	protected JAXBElement<String> version;

	/**
	 * Gets the value of the amtIn property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getAmtIn() {
		return amtIn;
	}

	/**
	 * Sets the value of the amtIn property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setAmtIn(Double value) {
		this.amtIn = value;
	}

	/**
	 * Gets the value of the amtOut property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getAmtOut() {
		return amtOut;
	}

	/**
	 * Sets the value of the amtOut property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setAmtOut(Double value) {
		this.amtOut = value;
	}

	/**
	 * Gets the value of the bankTxSerNo property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getBankTxSerNo() {
		return bankTxSerNo;
	}

	/**
	 * Sets the value of the bankTxSerNo property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setBankTxSerNo(JAXBElement<String> value) {
		this.bankTxSerNo = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the code property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getCode() {
		return code;
	}

	/**
	 * Sets the value of the code property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setCode(JAXBElement<String> value) {
		this.code = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the countIn property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getCountIn() {
		return countIn;
	}

	/**
	 * Sets the value of the countIn property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setCountIn(Integer value) {
		this.countIn = value;
	}

	/**
	 * Gets the value of the countOut property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getCountOut() {
		return countOut;
	}

	/**
	 * Sets the value of the countOut property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setCountOut(Integer value) {
		this.countOut = value;
	}

	/**
	 * Gets the value of the document property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}
	 *         {@link XMLDocument }{@code >}
	 * 
	 */
	public JAXBElement<XMLDocument> getDocument() {
		return document;
	}

	/**
	 * Sets the value of the document property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}
	 *            {@link XMLDocument }{@code >}
	 * 
	 */
	public void setDocument(JAXBElement<XMLDocument> value) {
		this.document = ((JAXBElement<XMLDocument>) value);
	}

	/**
	 * Gets the value of the inoutDetails property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}
	 *         {@link ArrayOfInOutDetailCheckEntry }{@code >}
	 * 
	 */
	public JAXBElement<ArrayOfInOutDetailCheckEntry> getInoutDetails() {
		return inoutDetails;
	}

	/**
	 * Sets the value of the inoutDetails property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}
	 *            {@link ArrayOfInOutDetailCheckEntry }{@code >}
	 * 
	 */
	public void setInoutDetails(JAXBElement<ArrayOfInOutDetailCheckEntry> value) {
		this.inoutDetails = ((JAXBElement<ArrayOfInOutDetailCheckEntry>) value);
	}

	/**
	 * Gets the value of the language property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getLanguage() {
		return language;
	}

	/**
	 * Sets the value of the language property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setLanguage(JAXBElement<String> value) {
		this.language = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the merchantTrnxNo property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getMerchantTrnxNo() {
		return merchantTrnxNo;
	}

	/**
	 * Sets the value of the merchantTrnxNo property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setMerchantTrnxNo(JAXBElement<String> value) {
		this.merchantTrnxNo = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the message property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getMessage() {
		return message;
	}

	/**
	 * Sets the value of the message property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setMessage(JAXBElement<String> value) {
		this.message = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the serverTime property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getServerTime() {
		return serverTime;
	}

	/**
	 * Sets the value of the serverTime property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setServerTime(JAXBElement<String> value) {
		this.serverTime = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the signFlag property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getSignFlag() {
		return signFlag;
	}

	/**
	 * Sets the value of the signFlag property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setSignFlag(JAXBElement<String> value) {
		this.signFlag = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the signature property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getSignature() {
		return signature;
	}

	/**
	 * Sets the value of the signature property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setSignature(JAXBElement<String> value) {
		this.signature = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the signatureAlgorithm property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	/**
	 * Sets the value of the signatureAlgorithm property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setSignatureAlgorithm(JAXBElement<String> value) {
		this.signatureAlgorithm = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the successed property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isSuccessed() {
		return successed;
	}

	/**
	 * Sets the value of the successed property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setSuccessed(Boolean value) {
		this.successed = value;
	}

	/**
	 * Gets the value of the trnxCode property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getTrnxCode() {
		return trnxCode;
	}

	/**
	 * Sets the value of the trnxCode property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setTrnxCode(JAXBElement<String> value) {
		this.trnxCode = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the version property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getVersion() {
		return version;
	}

	/**
	 * Sets the value of the version property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setVersion(JAXBElement<String> value) {
		this.version = ((JAXBElement<String>) value);
	}

}
