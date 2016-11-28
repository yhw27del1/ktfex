package com.kmfex.webservice.client.hx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out" type="{http://response.B2B.hitrust.com}ResponseDZ016"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "out" })
@XmlRootElement(name = "signOffResponse")
public class SignOffResponse {

	@XmlElement(required = true, nillable = true)
	protected ResponseDZ016 out;

	/**
	 * Gets the value of the out property.
	 * 
	 * @return possible object is {@link ResponseDZ016 }
	 * 
	 */
	public ResponseDZ016 getOut() {
		return out;
	}

	/**
	 * Sets the value of the out property.
	 * 
	 * @param value
	 *            allowed object is {@link ResponseDZ016 }
	 * 
	 */
	public void setOut(ResponseDZ016 value) {
		this.out = value;
	}

}
