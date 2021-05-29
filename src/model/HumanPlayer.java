/**
 * Class represents the human player and inherits from abstract Player class.
 *
 * @author fpetek
 * @version 1.0
 */
package model;

import java.io.Serializable;

public class HumanPlayer extends Player implements Serializable, Cloneable {

  /**
   * constructor for a HumanPlayer with a Player parameter.
   *
   * @author socho, fpetek
   */
  public HumanPlayer(Player user) {
    this.setUserName(user.getUserName());
    this.setPlayerID(user.getPlayerID());
    this.setScore(user.getScore());
  }
  /**
   * constructor for a HumanPlayer with a String parameter.
   *
   * @author socho, fpetek
   */
  public HumanPlayer(String userName) {
    this.setUserName(userName);
    this.setPlayerID(0);
    this.setScore(0);
  }
}
