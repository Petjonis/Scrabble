/**
 * Class represents the human player and inherits from abstract Player class.
 *
 * @author fpetek
 * @version 1.0
 */

package model;

import java.io.Serializable;

public class HumanPlayer extends Player implements Serializable, Cloneable {

  public HumanPlayer(Player user){
   this.setUserName(user.getUserName());
   this.setPlayerID(user.getPlayerID());
   this.setActiveSession(user.getActiveSession());
   this.setBag(user.getBag());
   this.setRack(user.getRack());
   this.setScore(user.getScore());
   this.setPassCount(user.getPassCount());
  }

  public HumanPlayer(String userName, int id){
    this.setUserName(userName);
    this.setPlayerID(id);
    this.setActiveSession(null);
    this.setBag(null);
    this.setRack(null);
    this.setScore(0);
    this.setPassCount(0);
  }
  @Override
  public void startTurn() {
  }

  @Override
  public void endTurn() {}

  @Override
  public void pass() {
    this.incrementPassCount();
    this.endTurn();
  }

  @Override
  public void draw() {}

  @Override
  public void swap() {}

  @Override
  public void put() {}

}
