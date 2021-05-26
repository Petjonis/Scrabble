/**
 * This message will be sent if a player wants to exchange tiles between rack and bag.
 *
 * @author fpetek
 * @version 1.0
 */
package messages;

import model.Player;
import model.Tile;

public class SwapTilesMessage extends Message {

  private static final long serialVersionUID = 1L;
  private Tile[] tiles;

  /**
   * constructor for "swapping a tile" message.
   *
   * @param tiles are the specific tiles to swap from rack.
   * @param player is user who wants to swap tiles.
   */
  public SwapTilesMessage(Tile[] tiles, Player player) {
    super(MessageType.SWAP_TILES, player);
    this.tiles = tiles;
  }

  public Tile[] getTiles() {
    return this.tiles;
  }
}
