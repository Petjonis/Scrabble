package messages;

import model.Player;
import model.Tile;

public class StartGameFirstMessage extends Message {
  private static final long serialVersionUID = 1L;

  private Tile[] tiles;
  private boolean isActive;
  /**
   * constructor for message.
   *
   * @param player
   */
  public StartGameFirstMessage(Player player, Tile[] tiles, boolean isActive) {
    super(MessageType.STARTGAME_FIRST, player);
    this.tiles = tiles;
    this.isActive = isActive;
  }

  public Tile[] getTiles() {
    return this.tiles;
  }

  public boolean getActive() {
    return this.isActive;
  }
}
