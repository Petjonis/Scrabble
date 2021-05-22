package messages;

import model.Player;

public class PassMessage extends Message {

  private static final long serialVersionUID = 1L;
  private int id;

  /**
   * constructor for "pass turn" message.
   *
   * @param player is user.
   */
  public PassMessage(Player player, int idNumber) {
    super(MessageType.PASS_MESSAGE, player);
    this.id = idNumber;
  }

  public int getId() {
    return this.id;
  }
}
