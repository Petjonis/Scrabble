package messages;

import model.Player;

public class LeaveGameMessage extends Message {

  private static final long serialVersionUID = 1L;
  private String name;

  /**
   * constructor for message to leave the game.
   *
   * @param player is for the player
   * @param playerName is for player's name.
   */
  public LeaveGameMessage(Player player, String playerName) {
    super(MessageType.LEAVE_GAME, player);
    this.name = playerName;
  }

  public String getName() {
    return this.name;
  }
}
