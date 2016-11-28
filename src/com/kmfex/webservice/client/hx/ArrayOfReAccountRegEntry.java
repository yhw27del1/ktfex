package com.kmfex.webservice.client.hx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ArrayOfReAccountRegEntry complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfReAccountRegEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReAccountRegEntry" type="{http://entry.response.B2B.hitrust.com}ReAccountRegEntry" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfReAccountRegEntry", namespace = "http://entry.response.B2B.hitrust.com", propOrder = { "reAccountRegEntry" })
public class ArrayOfReAccountRegEntry {

	@XmlElement(name = "ReAccountRegEntry", nillable = true)
	protected List<ReAccountRegEntry> reAccountRegEntry;

	/**
	 * Gets the value of the reAccountRegEntry property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the reAccountRegEntry property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getReAccountRegEntry().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ReAccountRegEntry }
	 * 
	 * 
	 */
	public List<ReAccountRegEntry> getReAccountRegEntry() {
		if (reAccountRegEntry == null) {
			reAccountRegEntry = new ArrayList<ReAccountRegEntry>();
		}
		return this.reAccountRegEntry;
	}

}
