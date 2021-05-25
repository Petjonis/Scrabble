/**
 * This abstract class contains common attributes and methods for player types.
 *
 * @author fpetek
 * @version 1.0
 */

package model;

import java.io.Serializable;

public abstract class Player implements Serializable {

  private String username;
  private int playerID;
  private Tile [] rack;
  private int score;
  private int passCount;
  private boolean isPlaying = false ;

  public abstract void startTurn();

  public abstract void endTurn();

  public abstract void pass();

  public abstract void draw();

  public abstract void swap();

  public abstract void put();


  public void addScore(int addition) {
    this.score += addition;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getPassCount() {
    return passCount;
  }

  public void setPassCount(int passCount) {
    this.passCount = passCount;
  }

  public void incrementPassCount() {
    this.passCount++;
  }

  public void resetPassCount() {
    this.passCount = 0;
  }

  public Tile [] getRack() {
    return rack;
  }

  public void setRack(Tile [] rack) {
    this.rack = rack;
  }


  public String getUserName() {
    return username;
  }

  public void setUserName(String username) {
    this.username = username;
  }

  public int getPlayerID() {
    return this.playerID;
  }

  public void setPlayerID(int idNumber) {
    this.playerID = idNumber;
  }

  public void setPlaying(boolean onTurn) { this.isPlaying = onTurn; }

  public boolean getPlaying() { return this.isPlaying; }
}
