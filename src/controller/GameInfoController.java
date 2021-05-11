package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.sun.tools.javac.Main;
import java.io.IOException;
import java.util.ArrayList;
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
    * */
     chatList.getItems().add(MainController.mainController.getUser().getUserName() + ": " + sendText.getText());
    MainController.mainController.getConnection()
        .sendToServer(new SendChatMessage(MainController.mainController.getUser().getUserName(), sendText.getText()));
    sendText.clear();

    //chatList.getItems().add(MainController.mainController.getUser().getUserName()+ ": " + sendText.getText());
    //sendText.clear();

  }

  //populate lists with demo strings
  public void initialize(URL url, ResourceBundle resourceBundle) {
    gameInfoController = this;
    /**
     * TableView initialize with all players, but not 100% functional, only host sees himself and player who joins see himself either.
     * @author socho
     */
    if (MainController.mainController.getHosting()) {
      for (String player : new ArrayList<String>(
          MainController.mainController.getUser().getActiveSession().getServer().getClientNames())) {
        playerList.getItems().add(player);
      }
    }

    chatList.getItems().add("[System]: Hello " + MainController.mainController.getUser().getUserName() + "!");

  }

  /** getter method. */
  public JFXListView<String> getChatList(){
    return this.chatList;
  }

  public void setChatList(ArrayList<String> chatText){
    for (String text : chatText) {
      this.chatList.getItems().add(text);
    }
  }

  public JFXListView<String> getPlayerList(){
    return this.playerList;
  }

  public void updatePlayerList(ArrayList<String> players){
    for (String player : players) {
      this.playerList.getItems().add(player);
    }
  }

  public void updateChat(String from, String text) {
    this.chatList.getItems().add(from + ": " + text);
  }

  public GameInfoController getInstance(){
    return this.gameInfoController;
  }
}
