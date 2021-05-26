package messages;

import model.Player;

import java.util.ArrayList;

/**
 * this message will be sent when the player list has to be updated.
 *
 * @author socho
 */
public class UpdatePlayerListMessage extends Message {

  private static final long serialVersionUID = 1L;
  private ArrayList<Player> activePlayers;

  /**
   * constructor for message.
   *
   * @param player
   * @param newList updated player list
   */
  public UpdatePlayerListMessage(Player player, ArrayList<Player> newList) {
    super(MessageType.UPDATE_PLAYERLIST, player);
    this.activePlayers = newList;
  }

  public ArrayList<Player> getActivePlayers() {
    return this.activePlayers;
  }
}
