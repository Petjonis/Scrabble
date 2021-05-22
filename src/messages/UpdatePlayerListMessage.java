package messages;

import java.util.ArrayList;
import model.Player;

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
   * @param from
   * @param newList updated player list
   */
  public UpdatePlayerListMessage(Player from, ArrayList<Player> newList) {
    super(MessageType.UPDATE_PLAYERLIST, from);
    this.activePlayers = newList;
  }

  public ArrayList<Player> getActivePlayers() {
    return this.activePlayers;
  }
}
