package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.sun.tools.javac.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import messages.SendChatMessage;
import model.GameState;

public class GameInfoController implements Initializable {
  static GameInfoController gameInfoController;

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
  void send(ActionEvent event) throws IOException {
    //demo
   /** problems with "sendTo" method in Network.ServerProtocol.
    * @author socho
    * chatList.getItems().add(MainController.mainController.getUserName() + ": " + sendText.getText());
    MainController.mainController.getConnection()
        .sendToServer(new SendChatMessage(MainController.mainController
            .getUserName(), sendText.getText())); */

    chatList.getItems().add(MainController.mainController.getUserName()+ ": " + sendText.getText());
    sendText.clear();
  }

  //populate lists with demo strings
  public void initialize(URL url, ResourceBundle resourceBundle) {
    gameInfoController = this;
    /**
     * TableView initialize with all players, but not 100% functional, only host sees himself and player who joins see himself either.
     * @author socho
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

  /** getter method. */
  public JFXListView<String> getChatList(){
    return this.chatList;
  }

}
