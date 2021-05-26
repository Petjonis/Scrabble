package model;

/**
 * This class represents one square of the board. One board has 15*15 squares
 *
 * @author socho
 * @version 1.0
 */
public class Square {

  private SquareType type;
  private Tile tile;
  private boolean occupied;

  public Square() {
    this.type = SquareType.NO_BONUS;
    this.tile = null;
    this.occupied = false;
  }

  public boolean isOccupied() {
    return this.occupied;
  }

  public void setOccupied(boolean b) {
    this.occupied = b;
  }

  public SquareType getType() {
    return type;
  }

  public void setType(String type) {
    switch (type) {
      case "dl":
        this.type = SquareType.DOUBLE_LETTER;
        break;
      case "dw":
        this.type = SquareType.DOUBLE_WORD;
        break;
      case "tl":
        this.type = SquareType.TRIPLE_LETTER;
        break;
      case "tw":
        this.type = SquareType.TRIPLE_WORD;
        break;
      case "st":
        this.type = SquareType.START;
        break;
    }
  }

  public Tile getTile() {
    return tile;
  }

  public void setTile(Tile tile) {
    this.tile = tile;
  }
}
