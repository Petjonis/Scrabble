package controller;

import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import model.GameSession;
import model.Player;
import network.Client;
import network.Server;

public class MainController implements Initializable {

  public static MainController mainController;
  private Player user;
  private boolean loggedIn = false;
  private boolean hosting = false;
  private Server server;
  private Client connection;
  private GameSession gameSession;

  @FXML
  private JFXButton playButton;

  @FXML
  private JFXButton learnButton;

  @FXML
  private JFXButton rulebookButton;

  @FXML
  private JFXButton loginButton;

  @FXML
  private JFXButton signupButton;

  @FXML
  private JFXButton logoutButton;

  @FXML
  private BorderPane borderPane;

  @FXML
  private FlowPane startPane;


  @FXML
  private StackPane centerPane;

  @FXML
  private StackPane rightPane;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    mainController = this;

    /** Player not logged in, so "Logout"-button will not be visible.*/
    if (!mainController.getLoggedIn()) {
      this.logoutButton.setVisible(false);
      /**
       * @apiNote need buttons for changing username, password or avater and to make clear player is logged in, as "Welcome ____ !".
       *
       * */
    }
  }

  @FXML
  void logout(ActionEvent event) throws IOException {
    if (hosting) {
      getServer().stopServer();
    }

    System.out.println(getUser().getUserName() + ", you are logged out.");
    getConnection().disconnect();
    setLoggedIn(false);
    setUser(null);

    getLoginButton().setVisible(true);
    getSignupButton().setVisible(true);
    getLogoutButton().setVisible(false);

    centerPane.getChildren().clear();
    rightPane.getChildren().clear();
  }

  @FXML
  void openLearn(ActionEvent event) {

  }

  @FXML
  void openPlay(ActionEvent event) throws IOException {
    changePane(centerPane, "/view/GameBoard.fxml");
    changePane(rightPane, "/view/PlayOnline.fxml");

  }

  @FXML
  void openRulebook(ActionEvent event) {

  }

  @FXML
  void signup(ActionEvent event) {
    openNewWindow("/view/Register.fxml", "Register");
  }


  @FXML
  void login(ActionEvent event) {
    openNewWindow("/view/Login.fxml", "Login");
  }

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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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
   * getter and setter methods for all private attributes.
   */

  public StackPane getCenterPane() {
    return centerPane;
  }

  public StackPane getRightPane() {
    return rightPane;
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

  public void setUser(Player player) {
    this.user = player;
  }

  public Player getUser() {
    return this.user;
  }

  public boolean getLoggedIn() {
    return this.loggedIn;
  }

  public void setLoggedIn(boolean log) {
    this.loggedIn = log;
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

  public void setGameSession(GameSession session) {
    this.gameSession = session;
  }

  public GameSession getGameSession() {
    return this.gameSession;
  }

}
