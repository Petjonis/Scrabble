package messages;

import model.Player;
import model.Square;
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
  private Square[][] position;

  /**
   * constructor for "sending a tile" message
   *
   * @param tile is for what kind of tile will be moved.
   * @param pos  is for the position of the tile on the board
   * @param from is for the user who moved this tile.
   * @author socho
   */

  public SendTileMessage(Tile tile, Square[][] pos, Player from) {
    super(MessageType.SEND_TILE, from);
    this.sendingTile = tile;
    this.position = pos;
  }

  public Tile getTile() {
    return this.sendingTile;
  }

  public Square[][] getPosition() {
    return this.position;
  }

}
