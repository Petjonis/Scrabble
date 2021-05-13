package model;

import javafx.scene.paint.Color;

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
  private String color;

  public Square(){
    this.type = SquareType.NO_BONUS;
    this.tile = null;
    this.occupied = false;
    this.color = "#eeeed2";
  }

  public boolean isOccupied() {
    return this.occupied;
  }

  public SquareType getType() {
    return type;
  }

  public void setType(String type) {
    switch (type){
      case "dl":
        this.type = SquareType.DOUBLE_LETTER;
        this.color = "#b8cc69";
        break;
      case "dw":
        this.type = SquareType.DOUBLE_WORD;
        this.color = "#f4ceca";
        break;
      case "tl":
        this.type = SquareType.TRIPLE_LETTER;
        this.color = "#66c9e8";
        break;
      case "tw":
        this.type = SquareType.TRIPLE_WORD;
        this.color = "#ee3940";
        break;
      case "st":
        this.type = SquareType.START;
        this.color = "#f4ceca";
        break;
    }
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

  public void setOccupied() {
    this.occupied = true;
  }

  public String getColor() {
    return color;
  }

}
