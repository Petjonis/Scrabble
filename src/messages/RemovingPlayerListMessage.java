package messages;


import model.Player;

public class RemovingPlayerListMessage extends Message {

  private static final long serialVersionUID = 1L;
  private int id;
  private String name;
  /**
   * constructor for message.
   *
   * @param player is the user.
   */
  public RemovingPlayerListMessage(Player player, String playerName, int idNumber) {
    super(MessageType.REMOVE_PLAYERLIST, player);
    this.id = idNumber;
    this.name = playerName;
  }

  public int getId() {
    return this.id;
  }
  public String getName(){ return this.name; }
}
