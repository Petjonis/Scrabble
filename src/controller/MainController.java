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
  public Database db;
  private Player user;
  private boolean loggedIn = false;
  private boolean hosting = false;
  private boolean editSettings = false;
  private Server server;
  private Client connection;
  private GameSession gameSession;

  @FXML private JFXButton playButton;

  @FXML private JFXButton learnButton;

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
      this.playButton.setDisable(true);
      this.logoutButton.setVisible(false);
      this.welcomeLabel.setVisible(false);
      this.changeUsernameButton.setVisible(false);
      this.changePasswordButton.setVisible(false);
      this.deleteProfileButton.setVisible(false);
      this.editProfileIcon.setVisible(false);
      this.gameCountLabel.setVisible(false);
      this.winCountLabel.setVisible(false);
      this.loseCountLabel.setVisible(false);
      this.winRateLabel.setVisible(false);
      this.avgPointsLabel.setVisible(false);
      this.gameCount.setVisible(false);
      this.winCount.setVisible(false);
      this.loseCount.setVisible(false);
      this.winRate.setVisible(false);
      this.avgPoints.setVisible(false);
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

    getLoginButton().setVisible(true);
    getSignupButton().setVisible(true);
    getLogoutButton().setVisible(false);
    getWelcomeLabel().setVisible(false);
    this.changeUsernameButton.setVisible(false);
    this.changePasswordButton.setVisible(false);
    this.deleteProfileButton.setVisible(false);
    this.editProfileIcon.setVisible(false);
    this.gameCountLabel.setVisible(false);
    this.winCountLabel.setVisible(false);
    this.loseCountLabel.setVisible(false);
    this.winRateLabel.setVisible(false);
    this.avgPointsLabel.setVisible(false);
    this.gameCount.setVisible(false);
    this.winCount.setVisible(false);
    this.loseCount.setVisible(false);
    this.winRate.setVisible(false);
    this.avgPoints.setVisible(false);

    changePane(centerPane, "/view/Start.fxml");
    rightPane.getChildren().clear();
    playButton.setDisable(true);
  }

  @FXML
  void openLearn(ActionEvent event) throws IOException {
    /* TODO: add screenshots how to play the game */
  }

  @FXML
  void openPlay(ActionEvent event) throws IOException {
    changePane(centerPane, "/view/GameBoard.fxml");
    changePane(rightPane, "/view/PlayOnline.fxml");
  }

  @FXML
  void openRulebook(ActionEvent event) throws IOException {
    changePane(centerPane, "/view/RuleBook.fxml");
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
  public void editProfileClicked(MouseEvent event) {
    if (editSettings) {
      this.editSettings = false;
      this.changeUsernameButton.setVisible(false);
      this.changePasswordButton.setVisible(false);
      this.deleteProfileButton.setVisible(false);
    } else {
      this.editSettings = true;
      this.changeUsernameButton.setVisible(true);
      this.changePasswordButton.setVisible(true);
      this.deleteProfileButton.setVisible(true);
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
}
