/**
 * This abstract class contains common attributes and methods for player types.
 *
 * @author fpetek
 * @version 1.0
 */

package model;

public abstract class Player {
  private String username;
  private GameSession activeSession;
  private TileBag bag;
  private TileRack rack;
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

  public TileBag getBag() { return bag; }

  public void setBag(TileBag bag) { this.bag = bag; }

  public String getUserName() {
    return username;
  }

  public void setUserName(String username) {
    this.username = username;
  }

  public void setActiveSession(GameSession game) { this.activeSession = game; }

  public GameSession getActiveSession() { return this.activeSession; }

}
