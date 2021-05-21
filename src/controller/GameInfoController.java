package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import messages.LeaveGameMessage;
import messages.RequestPlayerListMessage;
import messages.SendChatMessage;

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

  @FXML
  private JFXButton leaveGameButton;

  /**
   * Listview "chatList" adds the text, which was written from the textfield "sendText" and the
   * user, who wrote the text. A new SendChatMessage with the text and the user who wrote it, will
   * be send to the server. Text area will be cleared after this, so the user can write a new text.
   *
   * @author socho
   */
  @FXML
  void send(ActionEvent event) throws IOException {
    if (MainController.mainController.getHosting()) {
      chatList.getItems()
          .add(MainController.mainController.getUser().getUserName() + " [Host]: " + sendText
              .getText());
      MainController.mainController.getConnection().sendToServer(
          new SendChatMessage(MainController.mainController.getUser().getUserName(),
              sendText.getText(), true));
    } else {
      chatList.getItems()
          .add(MainController.mainController.getUser().getUserName() + ": " + sendText.getText());
      MainController.mainController.getConnection()
          .sendToServer(new SendChatMessage(MainController.mainController.getUser().getUserName(),
              sendText.getText(), false));
    }
    sendText.clear();

  }

  /**
   * chatList adds a welcome message to the user who joined to the game session.
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    gameInfoController = this;

    if (MainController.mainController.getHosting()) {
      chatList.getItems()
          .add("[System]: " + MainController.mainController.getUser().getUserName()
              + ", you are the host!");
    } else {
      chatList.getItems()
          .add("[System]: Hello " + MainController.mainController.getUser().getUserName() + "!");
    }
    try {
      sendRequestMessage();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendRequestMessage() throws IOException {
    MainController.mainController.getConnection().sendToServer(
        new RequestPlayerListMessage(MainController.mainController.getUser().getUserName()));
  }

  /**
   * update methods for players list, last words list and chat.
   */

  public void updatePlayerList(ArrayList<String> players) {
    for (String player : players) {
      if (playerList.getItems().isEmpty()) {
        playerList.getItems().add(player);
      } else if (!playerList.getItems().isEmpty() && !playerList.getItems()
          .contains(player)) {
        playerList.getItems().add(player);
      }
    }
  }

  public void removePlayerFromPlayerList(String from) {
    if (playerList.getItems().contains(from)) {
      playerList.getItems().remove(from);
    }
  }

  public void updateLastWordList(String word) {
    lastWordList.getItems().add(word);
  }

  /**
   * updates chat, distinguishes between host and clients.
   *
   * @author socho
   */
  public void updateChat(String from, String text, boolean token) {
    if (token) {
      chatList.getItems().add(from + " [Host]: " + text);
    } else {
      chatList.getItems().add(from + ": " + text);
    }
  }

  /**
   * chat can create a new line with "TAB"-key and "ENTER"-key sends the text.
   *
   * @author socho
   */
  public void chatKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.TAB) {
      sendText.appendText("\n");
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      send(new ActionEvent());
    }
  }

  /**
   * Use Case 3.4 leaving game. you have to confirm to leave. if host leaves, server stops. if
   * clients leave, they will disconnect.
   *
   * @author socho
   */
  public void leave(ActionEvent actionEvent) throws IOException {
    Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Leaving game confirmation");
    confirmationAlert.setHeaderText("Do you want to leave this game session?");
    Optional<ButtonType> result = confirmationAlert.showAndWait();
    if (result.get() == ButtonType.OK) {
      MainController.mainController.getConnection().sendToServer(
          new LeaveGameMessage(MainController.mainController.getUser().getUserName()));
      if (MainController.mainController.getHosting()) {
        MainController.mainController.getServer().stopServer();
      }
      MainController.mainController.disconnect();
      MainController.mainController
          .changePane(MainController.mainController.getCenterPane(), "/view/Start.fxml");
      MainController.mainController.getRightPane().getChildren().clear();

      MainController.mainController.getPlayButton().setDisable(false);
    }
  }
}
