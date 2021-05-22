package messages;

import model.Player;

/**
 * DisconnectMessage will be sent if the client disconnects from the server.
 *
 * @author socho
 * @version 1.0
 */
public class DisconnectMessage extends Message {

  private static final long serialVersionUID = 1L;
  private int id;

  public DisconnectMessage(Player player, int idNumber) {
    super(MessageType.DISCONNECT, player);
    this.id = idNumber;
  }

  @Override
  public Object clone() {
    return super.clone();
  }

  public int getId() {
    return this.id;
  }
}