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
import messages.RequestPlayerListMessage;
import messages.SendChatMessage;
import messages.UpdatePlayerListMessage;

public class GameInfoController implements Initializable {

  public static GameInfoController gameInfoController;

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

  /**
   * Listview "chatList" adds the text, which was written from the textfield "sendText" and the
   * user, who wrote the text. A new SendChatMessage with the text and the user who wrote it, will
   * be send to the server. Textfield will be cleared after this, so the user can write a new text.
   *
   * @author socho
   */
  @FXML
  void send(ActionEvent event) throws IOException {
    chatList.getItems()
        .add(MainController.mainController.getUser().getUserName() + ": " + sendText.getText());
    MainController.mainController.getConnection()
        .sendToServer(new SendChatMessage(MainController.mainController.getUser().getUserName(),
            sendText.getText()));
    sendText.clear();
  }

  /** chatList adds a welcome message to the user who joined to the game session. */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    gameInfoController = this;

    chatList.getItems()
        .add("[System]: Hello " + MainController.mainController.getUser().getUserName() + "!");

    try {
      sendRequestMessage();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendRequestMessage() throws IOException {
    MainController.mainController.getConnection().sendToServer(new RequestPlayerListMessage(MainController.mainController.getUser().getUserName()));
  }

  /**
   * update methods for players list, last words list and chat.
   */

  public void updatePlayerList(ArrayList<String> players) {
    for (String player : players) {
      playerList.getItems().add(player);
    }
  }

  public void updatePlayers(String userName){
    playerList.getItems().add(userName);
  }

  public void updateChat(String from, String text) {
    chatList.getItems().add(from + ": " + text);
  }

}
