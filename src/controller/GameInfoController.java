package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import messages.LeaveGameMessage;
import messages.RequestPlayerListMessage;
import messages.SendChatMessage;
import messages.ShutDownMessage;
import messages.StartGameFirstMessage;
import model.Player;
import model.Tile;
import model.TileRack;

public class GameInfoController implements Initializable {

  public static GameInfoController gameInfoController;

  @FXML
  private AnchorPane gameInfoPane;

  @FXML
  private JFXListView<String> playerList;

  @FXML
  private JFXListView<Integer> scoreList;

  @FXML
  private Label lastPlayedWordsLabel;

  @FXML
  private JFXListView<String> lastWordList;

  @FXML
  private JFXButton startGameButton;

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
          new SendChatMessage(MainController.mainController.getUser(),
              sendText.getText(), true));
    } else {
      chatList.getItems()
          .add(MainController.mainController.getUser().getUserName() + ": " + sendText.getText());
      MainController.mainController.getConnection()
          .sendToServer(new SendChatMessage(MainController.mainController.getUser(),
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
      lastPlayedWordsLabel.setVisible(false);
      lastWordList.setVisible(false);
      chatList.getItems()
          .add("[System]: " + MainController.mainController.getUser().getUserName()
              + ", you are the host!");
    } else {
      startGameButton.setVisible(false);
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
        new RequestPlayerListMessage(MainController.mainController.getUser()));
  }

  /**
   * update methods for players list.
   *
   * @author socho
   */

  public void updatePlayerList(ArrayList<Player> players) {
    for (Player player : players) {
      if (playerList.getItems().isEmpty()) {
        playerList.getItems().add(player.getUserName());
        scoreList.getItems().add(player.getScore());
      } else if (!playerList.getItems().isEmpty() && !playerList.getItems()
          .contains(player.getUserName())) {
        playerList.getItems().add(player.getUserName());
        scoreList.getItems().add(player.getScore());
      }
    }
  }

  public void addScoreToLeaderBoard(int score){
    scoreList.getItems().add(score);
  }
  /**
   * removing player from player list.
   *
   * @author socho
   */
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
  public void updateChat(Player from, String text, boolean token) {
    if (token) {
      chatList.getItems().add(from.getUserName() + " [Host]: " + text);
    } else {
      chatList.getItems().add(from.getUserName() + ": " + text);
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
      switch (String.valueOf(MainController.mainController.getHosting())) {
        case "true":
          MainController.mainController.getConnection().sendToServer(
              new ShutDownMessage(MainController.mainController.getUser(),
                  MainController.mainController.getUser().getPlayerID()));

          MainController.mainController
              .changePane(MainController.mainController.getCenterPane(), "/view/Start.fxml");
          MainController.mainController.getRightPane().getChildren().clear();

          MainController.mainController.getPlayButton().setDisable(false);
          break;
        case "false":
          MainController.mainController.getConnection().sendToServer(
              new LeaveGameMessage(MainController.mainController.getUser(),
                  MainController.mainController.getUser().getPlayerID()));
          MainController.mainController.disconnect();

          MainController.mainController
              .changePane(MainController.mainController.getCenterPane(), "/view/Start.fxml");
          MainController.mainController.getRightPane().getChildren().clear();
          MainController.mainController.getPlayButton().setDisable(false);
          break;
        default:
          break;
      }
    }
  }

  public void startGame(ActionEvent actionEvent) throws IOException {
    if (MainController.mainController.getHosting()){
      lastPlayedWordsLabel.setVisible(true);
      lastWordList.setVisible(true);
      startGameButton.setVisible(false);
    }

    for (Player player : MainController.mainController.getGameSession().getPlayers()){
      TileRack playerTiles = new TileRack(MainController.mainController.getGameSession().getTilebag());

      Tile [] tileRack = new Tile[7];
      playerTiles.getTileRack().toArray(tileRack);
      player.setRack(tileRack);
      boolean isActive = MainController.mainController.getGameSession().getPlayers().indexOf(player) == 1;
      MainController.mainController.getServer().getClientsHashMap().get(player).sendToClient(new StartGameFirstMessage(player, tileRack, isActive));
    }

  }
}
