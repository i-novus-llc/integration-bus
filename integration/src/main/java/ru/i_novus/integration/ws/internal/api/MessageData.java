package ru.i_novus.integration.ws.internal.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageData", propOrder = {
        "groupUid",
        "uuid",
        "appData"
})
public class MessageData implements Serializable {

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
