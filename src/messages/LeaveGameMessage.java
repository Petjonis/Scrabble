package messages;

import model.Player;

public class LeaveGameMessage extends Message {

  private static final long serialVersionUID = 1L;
  private int id;

  /**
   * constructor for message to leave the game.
   *
   * @param player   is for the player
   * @param idNumber is for player's id.
   */
  public LeaveGameMessage(Player player, int idNumber) {
    super(MessageType.LEAVE_GAME, player);
    this.id = idNumber;
  }

  public int getId() {
    return this.id;
  }
}
