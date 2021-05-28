/**
 * This abstract class contains common attributes and methods for player types.
 *
 * @author fpetek
 * @version 1.0
 */
package model;

import java.io.Serializable;
import java.util.Comparator;

public abstract class Player implements Serializable {

  public static Comparator<Player> Score =
      new Comparator<Player>() {
        @Override
        public int compare(Player p1, Player p2) {
          int s1 = p1.getScore();
          int s2 = p2.getScore();
          return s2 - s1;
        }
      };
  private String username;
  private int playerID;
  private int score;
  private boolean isPlaying = false;

  public void addScore(int addition) {
    this.score += addition;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
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

  public boolean getPlaying() {
    return this.isPlaying;
  }

  public void setPlaying(boolean onTurn) {
    this.isPlaying = onTurn;
  }
}
