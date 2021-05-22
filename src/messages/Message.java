package messages;

/**
 * This abstract class represents the messages which will be send between the server and client(s).
 *
 * @author socho
 * @version 1.0
 */

import java.io.Serializable;
import model.HumanPlayer;
import model.Player;

public abstract class Message implements Serializable, Cloneable {

  private static final long serialVersionUID = 1L;

  private MessageType meType;
  private Player from;

  /**
   * constructor for message.
   */
  public Message(MessageType type, Player from) {
    this.meType = type;
    this.from = new HumanPlayer(from);
  }

  public MessageType getMessageType() {
    return this.meType;
  }

  public Player getFrom() {
    return this.from;
  }

  public void setFrom(Player name) {
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
    clone.from = new HumanPlayer(from);
    return clone;
  }
}