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
 *         &lt;element name="json" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "json" })
@XmlRootElement(name = "inOutGoldDetailedCheck")
public class InOutGoldDetailedCheck {

	@XmlElement(required = true, nillable = true)
	protected String json;

	/**
	 * Gets the value of the json property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getJson() {
		return json;
	}

	/**
	 * Sets the value of the json property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setJson(String value) {
		this.json = value;
	}

}
