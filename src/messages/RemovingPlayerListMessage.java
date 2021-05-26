package messages;

import model.Player;

public class RemovingPlayerListMessage extends Message {

  private static final long serialVersionUID = 1L;
  private String name;
  /**
   * constructor for message.
   *
   * @param player is the user.
   */
  public RemovingPlayerListMessage(Player player, String playerName) {
    super(MessageType.REMOVE_PLAYERLIST, player);
    this.name = playerName;
  }

  public String getName() {
    return this.name;
  }
}
