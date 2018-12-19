package ru.i_novus.integration.ws.internal.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageInfo", propOrder = {
        "sender",
        "recipient",
        "date",
        "messageId"
})
public class MessageInfo implements Serializable {

    @XmlElement(name = "Sender")
    protected String sender;
    @XmlElement(name = "Recipient")
    protected String recipient;
    @XmlElement(name = "Date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "MessageId")
    protected String messageId;

    /**
     * Gets the value of the sender property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSender(String value) {
        this.sender = value;
    }

    /**
     * Gets the value of the recipient property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Sets the value of the recipient property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRecipient(String value) {
        this.recipient = value;
    }

    /**
     * Gets the value of the date property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the messageId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

}
