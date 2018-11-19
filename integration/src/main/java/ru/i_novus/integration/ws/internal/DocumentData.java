package ru.i_novus.integration.ws.internal;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentData complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DocumentData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DocName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DigestData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BinaryData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="DocType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="RemovePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentData", propOrder = {
        "docName",
        "docFormat",
        "digestData",
        "binaryData",
        "docType",
        "removePath"
})
public class DocumentData {

    @XmlElement(name = "DocName")
    protected String docName;
    @XmlElement(name = "DocFormat")
    protected String docFormat;
    @XmlElement(name = "DigestData")
    protected String digestData;
    @XmlElement(name = "BinaryData")
    @XmlMimeType("application/octet-stream")
    protected DataHandler binaryData;
    @XmlElement(name = "DocType")
    protected Integer docType;
    @XmlElement(name = "RemovePath")
    protected String removePath;

    /**
     * Gets the value of the docName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDocName() {
        return docName;
    }

    /**
     * Sets the value of the docName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDocName(String value) {
        this.docName = value;
    }

    /**
     * Gets the value of the docFormat property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDocFormat() {
        return docFormat;
    }

    /**
     * Sets the value of the docFormat property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDocFormat(String value) {
        this.docFormat = value;
    }

    /**
     * Gets the value of the digestData property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDigestData() {
        return digestData;
    }

    /**
     * Sets the value of the digestData property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDigestData(String value) {
        this.digestData = value;
    }

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
     * Gets the value of the docType property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getDocType() {
        return docType;
    }

    /**
     * Sets the value of the docType property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setDocType(Integer value) {
        this.docType = value;
    }

    /**
     * Gets the value of the removePath property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRemovePath() {
        return removePath;
    }

    /**
     * Sets the value of the removePath property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRemovePath(String value) {
        this.removePath = value;
    }

}
