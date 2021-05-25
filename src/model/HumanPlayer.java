/**
 * Class represents the human player and inherits from abstract Player class.
 *
 * @author fpetek
 * @version 1.0
 */

package model;

import controller.GameBoardController;
import controller.GameInfoController;
import java.io.Serializable;

public class HumanPlayer extends Player implements Serializable, Cloneable {

  public HumanPlayer(Player user){
   this.setUserName(user.getUserName());
   this.setPlayerID(user.getPlayerID());
   this.setBag(user.getBag());
   this.setRack(user.getRack());
   this.setScore(user.getScore());
   this.setPassCount(user.getPassCount());
  }

  public HumanPlayer(String userName, int id){
    this.setUserName(userName);
    this.setPlayerID(id);
    this.setBag(null);
    this.setRack(null);
    this.setScore(0);
    this.setPassCount(0);
  }
  @Override
  public void startTurn() {
    setPlaying(true);
    GameBoardController.gameBoardController.getPlayButton().setDisable(false);
    GameBoardController.gameBoardController.getPassButton().setDisable(false);
    GameBoardController.gameBoardController.getSwapButton().setDisable(false);
    GameBoardController.gameBoardController.getUndoButton().setDisable(false);
    GameBoardController.gameBoardController.startProgressBar();
  }

  @Override
  public void endTurn() {
    setPlaying(false);
    GameBoardController.gameBoardController.getPlayButton().setDisable(false);
    GameBoardController.gameBoardController.getPassButton().setDisable(false);
    GameBoardController.gameBoardController.getSwapButton().setDisable(false);
    GameBoardController.gameBoardController.getUndoButton().setDisable(false);
  }

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
