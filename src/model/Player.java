/**
 * This abstract class contains common attributes and methods for player types.
 *
 * @author fpetek
 * @version 1.0
 **/
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
}
