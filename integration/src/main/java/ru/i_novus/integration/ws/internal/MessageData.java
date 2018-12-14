
package ru.i_novus.integration.ws.internal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageData complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MessageData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="groupUid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Uuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AppData" type="{http://ws.integration.i_novus.ru/internal}DocumentData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageData", propOrder = {
        "groupUid",
        "uuid",
        "appData"
})
public class MessageData {

    protected String groupUid;
    @XmlElement(name = "Uuid")
    protected String uuid;
    @XmlElement(name = "AppData")
    protected DocumentData appData;

    /**
     * Gets the value of the groupUid property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getGroupUid() {
        return groupUid;
    }

    /**
     * Sets the value of the groupUid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setGroupUid(String value) {
        this.groupUid = value;
    }

    /**
     * Gets the value of the uuid property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the appData property.
     *
     * @return
     *     possible object is
     *     {@link DocumentData }
     *
     */
    public DocumentData getAppData() {
        return appData;
    }

    /**
     * Sets the value of the appData property.
     *
     * @param value
     *     allowed object is
     *     {@link DocumentData }
     *
     */
    public void setAppData(DocumentData value) {
        this.appData = value;
    }

}
