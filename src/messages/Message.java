package messages;

/**
 * This abstract class represents the messages which will be send between the server and client(s).
 *
 * @author socho
 * @version 1.0
 */
import model.HumanPlayer;
import model.Player;

import java.io.Serializable;

public abstract class Message implements Serializable, Cloneable {

  private static final long serialVersionUID = 1L;

  private MessageType meType;
  private Player playerUser;

  /** constructor for message. */
  public Message(MessageType type, Player player) {
    this.meType = type;
    this.playerUser = player;
  }

  public MessageType getMessageType() {
    return this.meType;
  }

  public Player getPlayer() {
    return this.playerUser;
  }

  public void setPlayer(Player playerClient) {
    this.playerUser = playerClient;
  }

  /** method to copy a message. */
  public Object clone() {
    Message clone = null;
    try {
      clone = (Message) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    clone.meType = meType;
    clone.playerUser = new HumanPlayer(playerUser);
    return clone;
  }
}
