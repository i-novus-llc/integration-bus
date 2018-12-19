package ru.i_novus.integration.ws.internal.api;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SplitDocumentModel", propOrder = {
        "binaryData",
        "temporaryPath",
        "count",
        "isLast"
})
public class SplitDocumentModel implements Serializable {

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
