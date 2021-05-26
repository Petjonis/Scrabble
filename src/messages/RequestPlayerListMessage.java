package messages;

import model.Player;

public class RequestPlayerListMessage extends Message {

  private static final long serialVersionUID = 1L;

  /**
   * constructor for message. request for getting an update for the player list, which shows all
   * players who joined the game session.
   *
   * @param player
   * @author socho
   */
  public RequestPlayerListMessage(Player player) {
    super(MessageType.REQUEST_PLAYERLIST, player);
  }
}
