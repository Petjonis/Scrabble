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
  private boolean occupied = false;

  public Square(SquareType type, Tile tile) {
    this.setType(type);
    this.setTile(tile);
  }

  public boolean isOccupied() {
    if (this.tile == null) {
      return false;
    } else {
      return true;
    }
  }

  public SquareType getType() {
    return type;
  }

  public void setType(SquareType type) {
    this.type = type;
  }

  public Tile getTile() {
    return tile;
  }

  public void setTile(Tile tile) {
    this.tile = tile;
  }

  public boolean getOccupied() {
    return this.occupied;
  }

  public void setOccupied(boolean b) {
    this.occupied = b;
  }

}
