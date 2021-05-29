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

  /**
   * method for adding score to a Player instance.
   *
   * @author fpetek
   */
  public void addScore(int addition) {
    this.score += addition;
  }

  /**
   * getter method for "score" int.
   *
   * @author fpetek
   */
  public int getScore() {
    return score;
  }

  /**
   * setter method for "score" int.
   *
   * @author fpetek
   */
  public void setScore(int score) {
    this.score = score;
  }

  /**
   * getter method for "userName" String from a Player instance.
   *
   * @author fpetek
   */
  public String getUserName() {
    return username;
  }

  /**
   * setter method for "userName" String from a Player instance.
   *
   * @author fpetek
   */
  public void setUserName(String username) {
    this.username = username;
  }

  /**
   * getter method for "playerID" int from a Player instance.
   *
   * @author fpetek
   */
  public int getPlayerID() {
    return this.playerID;
  }

  /**
   * setter method for "playerID" int from a Player instance.
   *
   * @author fpetek
   */
  public void setPlayerID(int idNumber) {
    this.playerID = idNumber;
  }

  /**
   * getter method for "isPlaying" boolean from a Player instance.
   *
   * @author fpetek
   */
  public boolean getPlaying() {
    return this.isPlaying;
  }

  /**
   * setter method for "isPlaying" boolean from a Player instance.
   *
   * @author fpetek
   */
  public void setPlaying(boolean onTurn) {
    this.isPlaying = onTurn;
  }
}
