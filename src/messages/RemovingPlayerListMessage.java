package messages;


import model.Player;

public class RemovingPlayerListMessage extends Message {

  private static final long serialVersionUID = 1L;
  private int id;

  /**
   * constructor for message.
   *
   * @param player is the user.
   */
  public RemovingPlayerListMessage(Player player, int idNumber) {
    super(MessageType.REMOVE_PLAYERLIST, player);
    this.id = idNumber;
  }

  public int getId() {
    return this.id;
  }

}
