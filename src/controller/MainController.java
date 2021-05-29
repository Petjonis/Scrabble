/**
 * Class to control stuff.
 *
 * @author fjaehrli
 * @author fpetek
 * @author socho
 * @version 1.0
 */

package controller;

import com.jfoenix.controls.JFXButton;
import db.Database;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.GameSession;
import model.Player;
import network.Client;
import network.Server;

public class MainController implements Initializable {

  public static MainController mainController;
  public Database db = new Database();
  private Player user;
  private boolean loggedIn = false;
  private boolean hosting = false;
  private boolean editSettings = false;
  private Server server;
  private Client connection;
  private GameSession gameSession;

  private Parent gameBoard;
  private Parent ruleBook;

  @FXML public VBox settingsView;

  @FXML public VBox loggedInView;

  @FXML private JFXButton playButton;

  @FXML private JFXButton rulebookButton;

  @FXML private JFXButton deleteProfileButton;

  @FXML private JFXButton changeUsernameButton;

  @FXML private JFXButton changePasswordButton;

  @FXML private JFXButton loginButton;

  @FXML private JFXButton signupButton;

  @FXML private JFXButton logoutButton;

  @FXML private Label welcomeLabel;

  @FXML private Label gameCountLabel;

  @FXML private Label winCountLabel;

  @FXML private Label loseCountLabel;

  @FXML private Label winRateLabel;

  @FXML private Label avgPointsLabel;

  @FXML private Label gameCount;

  @FXML private Label winCount;

  @FXML private Label loseCount;

  @FXML private Label winRate;

  @FXML private Label avgPoints;

  @FXML private FontAwesomeIconView editProfileIcon;

  @FXML private BorderPane borderPane;

  @FXML private FlowPane startPane;

  @FXML private StackPane centerPane;

  @FXML private StackPane rightPane;

  /**
   * Initializes "Main" view.
   *
   * @param url Gets called automatically.
   * @param resourceBundle Gets called automatically.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    mainController = this;

    /** Player not logged in, so "Logout"-button, Statistics and edit-button will not be visible. */
    if (!mainController.getLoggedIn()) {
      this.logoutButton.setVisible(false);
      loggedInView.setVisible(false);
      settingsView.setVisible(false);
    }

    try {
      // getting loader and a pane for the GameBoard.
      FXMLLoader gameBoardLoader = new FXMLLoader(getClass().getResource("/view/GameBoard.fxml"));
      gameBoard = gameBoardLoader.load();

      // getting loader and a pane for the Rule Book.
      FXMLLoader secondPaneLoader = new FXMLLoader(getClass().getResource("/view/RuleBook.fxml"));
      ruleBook = secondPaneLoader.load();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to log a user out of the game and to set stuff visible/invisible.
   *
   * @param event Listens on logoutButton.
   * @throws IOException Nothing to worry about.
   */
  @FXML
  void logout(ActionEvent event) throws IOException {
    if (hosting) {
      getServer().stopServer();
    }

    System.out.println(getUser().getUserName() + ", you are logged out.");
    if (getConnection() != null) {
      getConnection().disconnect();
    }
    setLoggedIn(false);
    setUser(null);

    loginButton.setVisible(true);
    loginButton.setFocusTraversable(true);
    signupButton.setVisible(true);
    signupButton.setFocusTraversable(true);
    logoutButton.setVisible(false);
    settingsView.setVisible(false);
    loggedInView.setVisible(false);
    loggedInView.setFocusTraversable(false);

    changePane(centerPane, "/view/Start.fxml");
    rightPane.getChildren().clear();
    playButton.setDisable(false);
    rulebookButton.setDisable(false);
  }

  @FXML
  void openPlay(ActionEvent event) throws IOException {
    centerPane.getChildren().clear();
    centerPane.getChildren().add(gameBoard);
    if(rightPane.getChildren().isEmpty()){
      changePane(rightPane, "/view/PlayOnline.fxml");
    }
  }

  @FXML
  void openRulebook(ActionEvent event) throws IOException {
    centerPane.getChildren().clear();
    centerPane.getChildren().add(ruleBook);
  }

  @FXML
  void signup(ActionEvent event) {
    openNewWindow("/view/Register.fxml", "Register");
  }

  @FXML
  void login(ActionEvent event) {
    openNewWindow("/view/Login.fxml", "Login");
  }

  @FXML
  void changeUsername(ActionEvent event) {
    openNewWindow("/view/ChangeUsername.fxml", "ChangeUsername");
  }

  @FXML
  void changePassword(ActionEvent event) {
    openNewWindow("/view/ChangePassword.fxml", "ChangePassword");
  }

  @FXML
  void deleteAccount(ActionEvent event) {
    openNewWindow("/view/DeleteAccount.fxml", "ChangeAccount");
  }

  /** method for opening a new window. */
  public void openNewWindow(String filename, String title) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));
      Parent root1 = fxmlLoader.load();
      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initStyle(StageStyle.TRANSPARENT);
      stage.setTitle(title);
      Scene scene = new Scene(root1);
      scene.setFill(Color.TRANSPARENT);
      stage.setScene(scene);
      stage.show();
      Stage parentStage =
          (Stage) MainController.mainController.getCenterPane().getScene().getWindow();
      stage.setX(parentStage.getX() + parentStage.getWidth() / 2 - stage.getWidth() / 2);
      stage.setY(parentStage.getY() + parentStage.getHeight() / 2 - stage.getHeight() / 2);
      stage.hide();
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** method for changing the pane, especially for the center and right pane. */
  public void changePane(StackPane pane, String fxmlPath) throws IOException {
    pane.getChildren().clear();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
    Parent parent = fxmlLoader.load();
    pane.getChildren().add(parent);
    if(fxmlPath.contains("RuleBook")){
      ruleBook = parent;
    }else if(fxmlPath.contains("GameBoard")){
      gameBoard = parent;
    } else if(fxmlPath.contains("Result")){
      FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/view/GameBoard.fxml"));
      gameBoard = fxmlLoader1.load();
      playButton.setDisable(true);
      rulebookButton.setDisable(true);
    } else if(fxmlPath.contains("Start")) {
      FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/view/GameBoard.fxml"));
      gameBoard = fxmlLoader1.load();
    }
  }

  /**
   * method for connecting to the server.
   *
   * @author socho
   */
  public void connectToServer(String host, int port) throws IOException {
    this.connection = new Client(host, port);
    if (this.connection.isOk()) {
      this.connection.start();
    }
  }

  /**
   * method for disconnecting.
   *
   * @author socho
   */
  public void disconnect() throws IOException {
    if (this.connection != null) {
      connection.disconnect();
      connection = null;
    }
  }

  /**
   * Method to open/close edit account settings.
   *
   * @param event when user clicks on settings icon
   * @author fpetek
   */
  public void editProfileClicked(ActionEvent event) {
    if (editSettings) {
      this.editSettings = false;
      settingsView.setVisible(false);
    } else {
      this.editSettings = true;
      settingsView.setVisible(true);
    }
  }

  /**
   * Method to reload player stats on main view.
   *
   * @author fpetek
   */
  public void reloadStats() {
    db.connect();
    this.gameCount.setText(Integer.toString(db.getGames(user.getUserName())));
    this.winCount.setText(Integer.toString(db.getWins(user.getUserName())));
    this.loseCount.setText(Integer.toString(db.getLoses(user.getUserName())));
    this.winRate.setText(
        String.format(
                Locale.ENGLISH,
                "%3.1f",
                ((double) db.getWins(user.getUserName()) / (double) db.getGames(user.getUserName()))
                    * 100)
            + " %");
    this.avgPoints.setText(Integer.toString(db.getScore(user.getUserName())));
    db.disconnect();
  }

  /** getter and setter methods for all private attributes. */
  public StackPane getCenterPane() {
    return centerPane;
  }

  public StackPane getRightPane() {
    return rightPane;
  }

  public JFXButton getPlayButton() {
    return playButton;
  }

  public Label getWelcomeLabel() {
    return welcomeLabel;
  }

  public void setWelcomeLabel(String username) {
    welcomeLabel.setText(username);
  }

  public JFXButton getLoginButton() {
    return loginButton;
  }

  public JFXButton getSignupButton() {
    return signupButton;
  }

  public JFXButton getLogoutButton() {
    return logoutButton;
  }

  public Player getUser() {
    return this.user;
  }

  public void setUser(Player player) {
    this.user = player;
  }

  public boolean getLoggedIn() {
    return this.loggedIn;
  }

  public void setLoggedIn(boolean log) {
    this.loggedIn = log;
  }

  public boolean getHosting() {
    return this.hosting;
  }

  public void setHosting(boolean host) {
    this.hosting = host;
  }

  public Client getConnection() {
    return this.connection;
  }

  public Server getServer() {
    return this.server;
  }

  public void setServer(Server serv) {
    this.server = serv;
  }

  public GameSession getGameSession() {
    return this.gameSession;
  }

  public void setGameSession(GameSession session) {
    this.gameSession = session;
  }

  public JFXButton getDeleteProfileButton() {
    return deleteProfileButton;
  }

  public JFXButton getChangeUsernameButton() {
    return changeUsernameButton;
  }

  public JFXButton getChangePasswordButton() {
    return changePasswordButton;
  }

  public FontAwesomeIconView getEditProfileIcon() {
    return editProfileIcon;
  }

  public Label getGameCountLabel() {
    return gameCountLabel;
  }

  public Label getWinCountLabel() {
    return winCountLabel;
  }

  public Label getAvgPointsLabel() {
    return avgPointsLabel;
  }

  public Label getLoseCountLabel() {
    return loseCountLabel;
  }

  public Label getWinRateLabel() {
    return winRateLabel;
  }

  public Label getGameCount() {
    return gameCount;
  }

  public void setGameCount(String gameCount) {
    this.gameCount.setText(gameCount);
  }

  public Label getWinCount() {
    return winCount;
  }

  public void setWinCount(String winCount) {
    this.winCount.setText(winCount);
  }

  public Label getLoseCount() {
    return loseCount;
  }

  public void setLoseCount(String loseCount) {
    this.loseCount.setText(loseCount);
  }

  public Label getWinRate() {
    return winRate;
  }

  public void setWinRate(String winRate) {
    this.winRate.setText(winRate);
  }

  public Label getAvgPoints() {
    return avgPoints;
  }

  public void setAvgPoints(String avgScore) {
    this.avgPoints.setText(avgScore);
  }

  public JFXButton getRulebookButton() {
    return rulebookButton;
  }

}
