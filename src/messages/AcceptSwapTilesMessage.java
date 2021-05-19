package messages;

import model.Tile;

public class AcceptSwapTilesMessage extends Message{

  private Tile [] swapTiles;
  /**
   * constructor for message.
   * @param from is the user.
   * @param newTiles are all new tiles which the user will receive.
   */
  public AcceptSwapTilesMessage(String from, Tile [] newTiles) {
    super(MessageType.ACCEPT_SWAP_TILES, from);
    this.swapTiles = newTiles;
  }

  public Tile [] getSwapTiles(){
    return this.swapTiles;
  }
}
