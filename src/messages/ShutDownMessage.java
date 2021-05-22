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

  public ShutDownMessage(Player from) {
    super(MessageType.SERVERSHUTDOWN, from);
  }

  @Override
  public Object clone() {
    return super.clone();
  }
}