package messages;

import model.Player;

/**
 * ShutDownMessage will be sent to the clients if the server shuts down.
 *
 * @author socho
 * @version 1.0
 */
public class ShutDownMessage extends Message {

  private static final long serialVersionUID = 1L;
  private int id;

  public ShutDownMessage(Player player, int idNumber) {
    super(MessageType.SERVERSHUTDOWN, player);
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
