/**
 * Class represents the computer player and inherits from abstract Player class.
 *
 * @author fpetek
 * @version 1.0
 */

package model;

public class ComputerPlayer extends Player {

  public ComputerPlayer(Player user) {
    this.setUserName(user.getUserName());
    this.setPlayerID(user.getPlayerID());
    this.setScore(user.getScore());
  }

  public ComputerPlayer(String compName) {
    this.setUserName(compName);
    this.setPlayerID(0);
    this.setScore(0);
  }

}
