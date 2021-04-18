package messages;

import model.Tile;

/**
 * This message will be sent if a tile will be moved from the tile rack to the board.
 *
 * @author socho
 * @version 1.0
 */

public class SendTileMessage extends Message {

  private static final long serialVersionUID = 1L;
  private Tile sendingTile;
  private Tile[][] position;

  public SendTileMessage(Tile tile, Tile[][] pos, String from) {
    super(MessageType.SEND_TILE, from);
    this.sendingTile = tile;
    this.position = pos;
  }

  public Tile getTile() {
    return this.sendingTile;
  }

  public Tile[][] getPosition() {
    return this.position;
  }

}