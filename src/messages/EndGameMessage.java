package messages;

import model.Player;

public class EndGameMessage extends Message {

  private static final long serialVersionUID = 1L;

  /**
   * constructor for message.
   *
   * @param player
   */
  public EndGameMessage(Player player) {
    super(MessageType.END_GAME, player);
  }
}
