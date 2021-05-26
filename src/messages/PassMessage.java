package messages;

import model.Player;

public class PassMessage extends Message {

  private static final long serialVersionUID = 1L;

  /**
   * constructor for "pass turn" message.
   *
   * @param player is user.
   */
  public PassMessage(Player player) {
    super(MessageType.PASS_MESSAGE, player);

  }

}
