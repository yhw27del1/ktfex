package com.kmfex.webservice.client.hx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ArrayOfInOutDetailCheckEntry complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfInOutDetailCheckEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InOutDetailCheckEntry" type="{http://entry.response.B2B.hitrust.com}InOutDetailCheckEntry" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfInOutDetailCheckEntry", namespace = "http://entry.response.B2B.hitrust.com", propOrder = { "inOutDetailCheckEntry" })
public class ArrayOfInOutDetailCheckEntry {

	@XmlElement(name = "InOutDetailCheckEntry", nillable = true)
	protected List<InOutDetailCheckEntry> inOutDetailCheckEntry;

	/**
	 * Gets the value of the inOutDetailCheckEntry property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the inOutDetailCheckEntry property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getInOutDetailCheckEntry().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link InOutDetailCheckEntry }
	 * 
	 * 
	 */
	public List<InOutDetailCheckEntry> getInOutDetailCheckEntry() {
		if (inOutDetailCheckEntry == null) {
			inOutDetailCheckEntry = new ArrayList<InOutDetailCheckEntry>();
		}
		return this.inOutDetailCheckEntry;
	}

}
