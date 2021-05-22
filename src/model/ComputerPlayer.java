/**
 * Class represents the computer player and inherits from abstract Player class.
 *
 * @author fpetek
 * @version 1.0
 */

package model;

public class ComputerPlayer extends Player {

  public ComputerPlayer(Player user){
    this.setUserName(user.getUserName());
    this.setPlayerID(user.getPlayerID());
    this.setBag(user.getBag());
    this.setRack(user.getRack());
    this.setScore(user.getScore());
    this.setPassCount(user.getPassCount());
  }

  public ComputerPlayer(String compName){
  this.setUserName(compName);
  this.setPlayerID(0);
  this.setBag(null);
  this.setRack(null);
  this.setScore(0);
  this.setPassCount(0);
}
  @Override
  public void startTurn() {}

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
