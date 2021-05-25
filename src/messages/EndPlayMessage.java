package messages;

import model.Player;
import model.Tile;

public class EndPlayMessage extends Message {

  private static final long serialVersionUID = 1L;
  private Tile[] tileRack;

  /**
   * constructor for message.
   *
   * @param player is for the player.
   * @param tiles  is for the tile rack tiles.
   */

  public EndPlayMessage(Player player, Tile[] tiles) {
    super(MessageType.END_PLAY, player);
    this.tileRack = tiles;
  }

  public Tile[] getTiles() {
    return this.tileRack;
  }
}
