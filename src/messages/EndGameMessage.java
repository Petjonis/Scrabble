package messages;

import model.Player;

import java.util.ArrayList;

public class EndGameMessage extends Message {

  private static final long serialVersionUID = 1L;
  private ArrayList<Player> players;

  /**
   * constructor for message.
   *
   * @param player
   */
  public EndGameMessage(Player player, ArrayList<Player> playerList) {
    super(MessageType.END_GAME, player);
    players = playerList;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }
}
