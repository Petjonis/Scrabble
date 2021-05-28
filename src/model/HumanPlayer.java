/**
 * Class represents the human player and inherits from abstract Player class.
 *
 * @author fpetek
 * @version 1.0
 */
package model;

import controller.GameBoardController;

import java.io.Serializable;

public class HumanPlayer extends Player implements Serializable, Cloneable {

  public HumanPlayer(Player user) {
    this.setUserName(user.getUserName());
    this.setPlayerID(user.getPlayerID());
    this.setScore(user.getScore());
  }

  public HumanPlayer(String userName) {
    this.setUserName(userName);
    this.setPlayerID(0);
    this.setScore(0);
  }

}
