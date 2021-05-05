package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import model.GameState;

public class GameInfoController implements Initializable {

  @FXML
  private AnchorPane gameInfoPane;

  @FXML
  private JFXListView<String> playerList;

  @FXML
  private JFXListView<String> lastWordList;

  @FXML
  private JFXListView<String> chatList;

  @FXML
  private JFXTextArea sendText;

  @FXML
  private JFXButton sendButton;

  @FXML
  void send(ActionEvent event) {
    //demo
    chatList.getItems().add("Player2: " + sendText.getText());
    sendText.clear();
  }

  //populate lists with demo strings
  public void initialize(URL url, ResourceBundle resourceBundle) {
    /** TableView initialize with all players, but not 100% functional, only host sees himself and player who joins see himself either.
     * while (PlayOnlineController.playOnlineController.getGameSession().getState() == GameState.WAITING_LOBBY) {
    if (GameBoardController.gameBoardController.getPlayButton().isPressed()){
      PlayOnlineController.playOnlineController.getGameSession().setState(GameState.INGAME);
    }
  }*/

    for (String player : MainController.mainController.getPlayerList()) {
      playerList.getItems().add(player);
    }
    /**playerList.getItems().add("Player1");
    playerList.getItems().add("Player2");
    playerList.getItems().add("Player3");
    playerList.getItems().add("Player4"); */

    chatList.getItems().add("Player1: Hello");


  }
}
