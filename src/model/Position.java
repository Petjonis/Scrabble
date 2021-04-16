package model;

public class Position {

	private int xCoordinate;
	private int yCoordinate;

	public Position(int x, int y) {
		this.setxCoordinate(x);
		this.setyCoordinate(y);
	}

  public int getyCoordinate() {
    return yCoordinate;
  }

  public void setyCoordinate(int yCoordinate) {
    this.yCoordinate = yCoordinate;
  }

  public int getxCoordinate() {
    return xCoordinate;
  }

  public void setxCoordinate(int xCoordinate) {
    this.xCoordinate = xCoordinate;
  }

}
