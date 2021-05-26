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

  public HumanPlayer(Player user){
   this.setUserName(user.getUserName());
   this.setPlayerID(user.getPlayerID());
   this.setScore(user.getScore());
  }

  public HumanPlayer(String userName){
    this.setUserName(userName);
    this.setPlayerID(0);
    this.setScore(0);
  }

  @Override
  public void startTurn() {
    setPlaying(true);
    GameBoardController.gameBoardController.getTileRack().setDisable(false);
    GameBoardController.gameBoardController.getPlayButton().setDisable(false);
    GameBoardController.gameBoardController.getPassButton().setDisable(false);
    GameBoardController.gameBoardController.getSwapButton().setDisable(false);
    GameBoardController.gameBoardController.getUndoButton().setDisable(false);
    GameBoardController.gameBoardController.startProgressBar();
  }

  @Override
  public void endTurn() {
    setPlaying(false);
    GameBoardController.gameBoardController.getTileRack().setDisable(true);
    GameBoardController.gameBoardController.getPlayButton().setDisable(true);
    GameBoardController.gameBoardController.getPassButton().setDisable(true);
    GameBoardController.gameBoardController.getSwapButton().setDisable(true);
    GameBoardController.gameBoardController.getUndoButton().setDisable(true);
  }

  @Override
  public void pass() {
    this.endTurn();
  }

  @Override
  public void draw() {}

  @Override
  public void swap() {}

  @Override
  public void put() {}

}
