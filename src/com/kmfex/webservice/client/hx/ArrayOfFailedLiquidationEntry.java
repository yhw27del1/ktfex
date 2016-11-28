package com.kmfex.webservice.client.hx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ArrayOfFailedLiquidationEntry complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfFailedLiquidationEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FailedLiquidationEntry" type="{http://entry.response.B2B.hitrust.com}FailedLiquidationEntry" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfFailedLiquidationEntry", namespace = "http://entry.response.B2B.hitrust.com", propOrder = { "failedLiquidationEntry" })
public class ArrayOfFailedLiquidationEntry {

	@XmlElement(name = "FailedLiquidationEntry", nillable = true)
	protected List<FailedLiquidationEntry> failedLiquidationEntry;

	/**
	 * Gets the value of the failedLiquidationEntry property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the failedLiquidationEntry property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getFailedLiquidationEntry().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link FailedLiquidationEntry }
	 * 
	 * 
	 */
	public List<FailedLiquidationEntry> getFailedLiquidationEntry() {
		if (failedLiquidationEntry == null) {
			failedLiquidationEntry = new ArrayList<FailedLiquidationEntry>();
		}
		return this.failedLiquidationEntry;
	}

}
