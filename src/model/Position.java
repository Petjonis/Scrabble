package model;

public class Position {

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
