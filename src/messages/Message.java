package messages;

/**
 * This abstract class represents the messages which will be send between the server and client(s).
 *
 * @author socho
 * @version 1.0
 */

import java.io.Serializable;

public abstract class Message implements Serializable, Cloneable {

  private static final long serialVersionUID = 1L;

  private MessageType meType;
  private String from;

  /**
   * constructor for message.
   */
  public Message(MessageType type, String from) {
    this.meType = type;
    this.from = new String(from);
  }

  public MessageType getMessageType() {
    return this.meType;
  }

  public String getFrom() {
    return this.from;
  }

  public void setFrom(String name) {
    this.from = name;
  }

  /**
   * method to copy a message.
   */
  public Object clone() {
    Message clone = null;
    try {
      clone = (Message) super.clone();
    } catch (CloneNotSupportedException e) {
    }
    clone.meType = meType;
    clone.from = new String(from);
    return clone;
  }
}