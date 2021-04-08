package model;

/**
 * This abstract class contains common attributes and methods for player types.
 *
 * @author fpetek
 * @version 1.0
 */
public abstract class Player {
  private String username;
  private TileBag bag;
  private int score;
  private int passCount;

  public abstract void startTurn();

  /**
   * Method to end a turn, especially to pass a turn
   */
  public abstract void endTurn();

  public abstract void draw();

  public abstract void swap();

  public abstract void put();

}
