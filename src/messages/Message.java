package messages;

/**
 * This abstract class represents the messages which will be send between server and client
 * @author socho
 * @version 1.0
 */

import java.io.Serializable;

public abstract class Message implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;

    private MessageType mType;
    private String from;

    public Message(MessageType type, String from){
        this.mType = type;
        this.from = new String(from);
    }

    public MessageType getMessageType(){
        return this.mType;
    }

    public String getFrom(){
        return this.from;
    }

    public void setFrom(String name){
        this.from = name;
    }

    public Object clone(){
        Message clone = null ;
        try {
            clone = (Message) super.clone();
        }catch (CloneNotSupportedException e){}
        clone.mType = mType;
        clone.from = new String (from);
        return clone;
    }
}