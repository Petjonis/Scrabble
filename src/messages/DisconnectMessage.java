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

  public DisconnectMessage(Player from, int idNumber) {
    super(MessageType.DISCONNECT, from);
    this.id = idNumber;
  }

  @Override
  public Object clone() {
    return super.clone();
  }

  public int getId(){
    return this.id;
  }
}