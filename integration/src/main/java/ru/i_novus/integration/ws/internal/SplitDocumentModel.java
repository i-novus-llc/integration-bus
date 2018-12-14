
package ru.i_novus.integration.ws.internal;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SplitDocumentModel complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SplitDocumentModel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BinaryData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="TemporaryPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Count" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IsLast" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SplitDocumentModel", propOrder = {
        "binaryData",
        "temporaryPath",
        "count",
        "isLast"
})
public class SplitDocumentModel {

    @XmlElement(name = "BinaryData")
    @XmlMimeType("application/octet-stream")
    protected DataHandler binaryData;
    @XmlElement(name = "TemporaryPath")
    protected String temporaryPath;
    @XmlElement(name = "Count")
    protected Integer count;
    @XmlElement(name = "IsLast")
    protected Boolean isLast;

    /**
     * Gets the value of the binaryData property.
     *
     * @return
     *     possible object is
     *     {@link DataHandler }
     *
     */
    public DataHandler getBinaryData() {
        return binaryData;
    }

    /**
     * Sets the value of the binaryData property.
     *
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *
     */
    public void setBinaryData(DataHandler value) {
        this.binaryData = value;
    }

    /**
     * Gets the value of the temporaryPath property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTemporaryPath() {
        return temporaryPath;
    }

    /**
     * Sets the value of the temporaryPath property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTemporaryPath(String value) {
        this.temporaryPath = value;
    }

    /**
     * Gets the value of the count property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCount(Integer value) {
        this.count = value;
    }

    /**
     * Gets the value of the isLast property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIsLast() {
        return isLast;
    }

    /**
     * Sets the value of the isLast property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIsLast(Boolean value) {
        this.isLast = value;
    }

}
