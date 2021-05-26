package model;

import java.io.Serializable;

public class Position implements Serializable {

  private int xCoordinate;
  private int yCoordinate;

  public Position(int row, int col) {
    this.xCoordinate = row;
    this.yCoordinate = col;
  }

  public int getCol() {
    return yCoordinate;
  }

  public int getRow() {
    return xCoordinate;
  }
}
