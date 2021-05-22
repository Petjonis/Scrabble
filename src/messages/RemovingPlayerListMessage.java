package messages;


import java.util.ArrayList;
import model.Player;

public class RemovingPlayerListMessage extends Message{
  private static final long serialVersionUID = 1L;

  /**
   * constructor for message.
   *
   * @param from is the user.
   */
  public RemovingPlayerListMessage(Player from) {
    super(MessageType.REMOVE_PLAYERLIST, from);

  }

}
