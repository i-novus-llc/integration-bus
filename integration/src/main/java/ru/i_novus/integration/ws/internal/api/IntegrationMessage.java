package ru.i_novus.integration.ws.internal.api;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IntegrationMessage")
@XmlType(name = "IntegrationMessage", propOrder = {
        "messageData",
        "message"
})
public class IntegrationMessage implements Serializable {

    @XmlElement(name = "MessageData")
    protected MessageInfo messageData;
    @XmlElement(name = "Message")
    protected MessageData message;

    /**
     * Gets the value of the messageData property.
     *
     * @return possible object integration
     * {@link MessageInfo }
     */
    public MessageInfo getMessageData() {
        return messageData;
    }

    /**
     * Sets the value of the messageData property.
     *
     * @param value allowed object integration
     *              {@link MessageInfo }
     */
    public void setMessageData(MessageInfo value) {
        this.messageData = value;
    }

    /**
     * Gets the value of the message property.
     *
     * @return possible object integration
     * {@link MessageData }
     */
    public MessageData getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value allowed object integration
     *              {@link MessageData }
     */
    public void setMessage(MessageData value) {
        this.message = value;
    }

}
