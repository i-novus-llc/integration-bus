package ru.i_novus.integration.ws.internal.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentData", propOrder = {
        "docName",
        "docFormat",
        "digestData",
        "docType",
        "splitDocument"
})
public class DocumentData implements Serializable {

    @XmlElement(name = "DocName")
    protected String docName;
    @XmlElement(name = "DocFormat")
    protected String docFormat;
    @XmlElement(name = "DigestData")
    protected String digestData;
    @XmlElement(name = "DocType")
    protected Integer docType;
    @XmlElement(name = "SplitDocument")
    protected SplitDocumentModel splitDocument;

    /**
     * Gets the value of the docName property.
     *
     * @return
     *     possible object integration
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
     *     allowed object integration
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
     *     possible object integration
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
     *     allowed object integration
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
     *     possible object integration
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
     *     allowed object integration
     *     {@link String }
     *
     */
    public void setDigestData(String value) {
        this.digestData = value;
    }

    /**
     * Gets the value of the docType property.
     *
     * @return
     *     possible object integration
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
     *     allowed object integration
     *     {@link Integer }
     *
     */
    public void setDocType(Integer value) {
        this.docType = value;
    }

    /**
     * Gets the value of the splitDocument property.
     *
     * @return
     *     possible object integration
     *     {@link SplitDocumentModel }
     *
     */
    public SplitDocumentModel getSplitDocument() {
        return splitDocument;
    }

    /**
     * Sets the value of the splitDocument property.
     *
     * @param value
     *     allowed object integration
     *     {@link SplitDocumentModel }
     *
     */
    public void setSplitDocument(SplitDocumentModel value) {
        this.splitDocument = value;
    }

}
