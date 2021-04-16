/**
 * This abstract class contains common attributes and methods for player types.
 *
 * @author fpetek
 * @version 1.0
 */

package model;

public abstract class Player {
  private String username;
  private TileBag bag;
  private int score;
  private int passCount;

  public abstract void startTurn();

  public abstract void endTurn();

  public abstract void pass();

  public abstract void draw();

  public abstract void swap();

  public abstract void put();

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getPassCount() {
    return passCount;
  }

  public void incrementPassCount() {
    this.passCount++;
  }

  public void resetPassCount() {
    this.passCount = 0;
  }

  public TileBag getBag() {
    return bag;
  }

  public void setBag(TileBag bag) {
    this.bag = bag;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * This method initializes the Tilebag attribute with a field of Tiles.
   *
   * @param tiles is a Tile-Array with maximum of 100 Tiles.
   */
  public void createTileBag(Tile[] tiles) {
    this.bag.setTiles(tiles);
  }
}
