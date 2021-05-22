package messages;

import model.Player;

/**
 * ConnectMessage which will be sent if the client connects with the server successfully.
 *
 * @author socho
 * @version 1.0
 */
public class ConnectMessage extends Message {

  private static final long serialVersionUID = 1L;
  private int id;

  public ConnectMessage(Player player, int idNumber) {
    super(MessageType.CONNECT, player);
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