package messages;

import model.Player;
import model.Tile;

public class AcceptSwapTilesMessage extends Message {

  private Tile[] swapTiles;

  /**
   * constructor for message.
   *
   * @param player is the user.
   * @param newTiles are all new tiles which the user will receive.
   */
  public AcceptSwapTilesMessage(Player player, Tile[] newTiles) {
    super(MessageType.ACCEPT_SWAP_TILES, player);
    this.swapTiles = newTiles;
  }

  public Tile[] getSwapTiles() {
    return this.swapTiles;
  }
}
