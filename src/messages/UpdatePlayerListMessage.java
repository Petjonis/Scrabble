package messages;

import java.util.ArrayList;

/**
 * this message will be sent when the player list has to be updated.
 *
 * @author socho
 */

public class UpdatePlayerListMessage extends Message {

  private static final long serialVersionUID = 1L;
  private ArrayList<String> activePlayers;

  /**
   * constructor for message.
   *
   * @param from
   * @param newList updated player list
   */
  public UpdatePlayerListMessage(String from, ArrayList<String> newList) {
    super(MessageType.UPDATE_PLAYERLIST, from);
    this.activePlayers = newList;
  }

  public ArrayList<String> getActivePlayers() {
    return this.activePlayers;
  }
}
