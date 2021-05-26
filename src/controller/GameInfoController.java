package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;
import messages.*;
import model.Player;
import model.Tile;
import model.TileRack;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameInfoController implements Initializable {

  public static GameInfoController gameInfoController;

  @FXML private AnchorPane gameInfoPane;

  @FXML private JFXListView<String> playerList;

  @FXML private JFXListView<Integer> scoreList;

  @FXML private Label lastPlayedWordsLabel;

  @FXML private JFXListView<String> lastWordList;

  @FXML private JFXButton startGameButton;

  @FXML private ListView<Text> chatList;

  @FXML private JFXTextArea sendText;

  @FXML private JFXButton sendButton;

  @FXML private JFXButton leaveGameButton;

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
      MainController.mainController
          .getConnection()
          .sendToServer(
              new SendChatMessage(
                  MainController.mainController.getUser(), sendText.getText(), true));
    } else {
      MainController.mainController
          .getConnection()
          .sendToServer(
              new SendChatMessage(
                  MainController.mainController.getUser(), sendText.getText(), false));
    }
    sendText.clear();
  }

  /** chatList adds a welcome message to the user who joined to the game session. */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    gameInfoController = this;

    if (MainController.mainController.getHosting()) {
      lastPlayedWordsLabel.setVisible(false);
      lastWordList.setVisible(false);
      addTextToChat(
              "[System]: "
                  + MainController.mainController.getUser().getUserName()
                  + ", you are the host!");
    } else {
      startGameButton.setVisible(false);
      addTextToChat("[System]: Hello " + MainController.mainController.getUser().getUserName() + "!");
    }

    try {
      sendRequestMessage();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendRequestMessage() throws IOException {
    MainController.mainController
        .getConnection()
        .sendToServer(new RequestPlayerListMessage(MainController.mainController.getUser()));
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
      } else if (!playerList.getItems().isEmpty()
          && !playerList.getItems().contains(player.getUserName())) {
        playerList.getItems().add(player.getUserName());
        scoreList.getItems().add(player.getScore());
      }
    }
  }

  /**
   * removing player from player list and removing score from score list.
   *
   * @author socho
   */
  public void removePlayerFromPlayerList(String from) {
    if (playerList.getItems().contains(from)) {
      int index = playerList.getItems().indexOf(from);
      playerList.getItems().remove(from);
      scoreList.getItems().remove(index - 1);
    }
  }

  public void updateLastWordList(ArrayList<Pair<String, Integer>> playedWords) {
    for (Pair<String, Integer> p : playedWords) {
      lastWordList.getItems().add("Word: " + p.getKey() + ", Score: " + p.getValue());
    }
    lastWordList.scrollTo(lastWordList.getItems().size() - 1);
  }

  public void updateScoreBoard(
      int index, ArrayList<Pair<String, Integer>> playedWords, boolean isBonus) {
    int score = 0;
    for (Pair<String, Integer> p : playedWords) {
      score += p.getValue();
    }
    scoreList.getItems().set(index, score + scoreList.getItems().get(index));
    if (isBonus) {
      scoreList.getItems().set(index, 50 + scoreList.getItems().get(index));
    }
  }

  public void setActivePlayer(int nextPlayer) {
    scoreList.getSelectionModel().select(nextPlayer);
    playerList.getSelectionModel().select(nextPlayer);
  }

  /**
   * updates chat, distinguishes between host and clients.
   *
   * @author socho
   */
  public void updateChat(Player from, String text, boolean token) {
    if (token) {
      addTextToChat(from.getUserName() + " [Host]: " + text);
    } else {
      addTextToChat(from.getUserName() + ": " + text);
    }
    chatList.scrollTo(chatList.getItems().size() - 1);
  }


  public void addTextToChat(String s){
    Text text = new Text(s);
    text.setWrappingWidth(240);
    if(s.startsWith("[System]")){
      text.setFill(Color.web("#f03434"));
    } else{
      text.setFill(Color.web("#dbd9d7"));
    }
    chatList.getItems().add(text);
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
          MainController.mainController
              .getConnection()
              .sendToServer(
                  new ShutDownMessage(
                      MainController.mainController.getUser(),
                      MainController.mainController.getUser().getPlayerID()));

          MainController.mainController.changePane(
              MainController.mainController.getCenterPane(), "/view/Start.fxml");
          MainController.mainController.getRightPane().getChildren().clear();

          MainController.mainController.getPlayButton().setDisable(false);
          break;
        case "false":
          MainController.mainController
              .getConnection()
              .sendToServer(
                  new LeaveGameMessage(
                      MainController.mainController.getUser(),
                      MainController.mainController.getUser().getUserName()));
          MainController.mainController.disconnect();

          MainController.mainController.changePane(
              MainController.mainController.getCenterPane(), "/view/Start.fxml");
          MainController.mainController.getRightPane().getChildren().clear();
          MainController.mainController.getPlayButton().setDisable(false);
          break;
        default:
          break;
      }
    }
  }

  public void startGame(ActionEvent actionEvent) throws IOException {
    if (MainController.mainController.getHosting()) {
      lastPlayedWordsLabel.setVisible(true);
      lastWordList.setVisible(true);
      startGameButton.setVisible(false);
    }
    for (Player player : MainController.mainController.getGameSession().getPlayers()) {
      TileRack playerTiles =
          new TileRack(MainController.mainController.getGameSession().getTilebag());
      Tile[] tileRack = new Tile[7];
      playerTiles.getTileRack().toArray(tileRack);
      boolean isActive =
          MainController.mainController.getGameSession().getPlayers().indexOf(player) == 1;
      MainController.mainController
          .getServer()
          .getClientsHashMap()
          .get(player)
          .sendToClient(new StartGameFirstMessage(player, tileRack, isActive));
    }
  }
}
