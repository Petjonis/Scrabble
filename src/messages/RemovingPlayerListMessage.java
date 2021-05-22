package messages;


import java.util.ArrayList;
import model.Player;

public class RemovingPlayerListMessage extends Message{
  private static final long serialVersionUID = 1L;

  /**
   * constructor for message.
   *
   * @param player is the user.
   */
  public RemovingPlayerListMessage(Player player) {
    super(MessageType.REMOVE_PLAYERLIST, player);

  }

}
