package messages;

import model.Player;

public class LeaveGameMessage extends Message{
  private static final long serialVersionUID = 1L;
  private int id;
  /**
   * constructor for message.
   *
   * @param player
   */
  public LeaveGameMessage(Player player, int idNumber) {
    super(MessageType.LEAVE_GAME, player);
    this.id = idNumber;
  }

  public int getId(){
    return this.id;
  }
}
