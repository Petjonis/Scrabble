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
  private String name;

  public DisconnectMessage(Player player, String playerName) {
    super(MessageType.DISCONNECT, player);
    this.name = playerName;
  }

  @Override
  public Object clone() {
    return super.clone();
  }

  public String getName() {
    return this.name;
  }
}
