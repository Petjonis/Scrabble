package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import messages.ConnectMessage;
import model.GameSession;

public class PlayOnlineController implements Initializable {

  private int port;
  private GameSession gameSession;

  @FXML
  private TabPane playTabPane;

  @FXML
  private JFXButton fileChooserButton;

  @FXML
  private Label filePathLabel;

  @FXML
  private Label descriptionLabel;

  @FXML
  private TextField portNumberTextField;

  @FXML
  private Label portErrorLabel;

  @FXML
  private JFXButton hostButton;

  @FXML
  private TextField ipField;

  @FXML
  private TextField portField;

  @FXML
  private JFXButton joinButton;

  @FXML
  private Label errorLabel;

  @FXML
  void chooseFile(ActionEvent event) {

  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
    this.portErrorLabel.setVisible(false);
    this.portNumberTextField.setText("49152");
    this.portField.setText("49152");
  }

  /**
   * "Host"-button can only be pressed, when player is logged in. System recognizes host and sets
   * "Host"-token to the user and calls the hostGameSession()-method. Host will also be on the list,
   * which contains all players who are in the game session. System changes the view and server
   * waits for other clients to join.
   *
   * @author socho
   */
  @FXML
  void host(ActionEvent event) throws IOException {
    if (MainController.mainController.getLoggedIn() == true) {
      if (hostGameSession()) {
        System.out
            .println(MainController.mainController.getUser().getUserName() + ", you are the Host.");

        Runnable r = new Runnable() {
          @Override
          public void run() {
            try {
              MainController.mainController.getServer().listen();
            } catch (IOException ioException) {
              ioException.printStackTrace();
            }
          }
        };
        new Thread(r).start();

        MainController.mainController.connectToServer("localhost", this.port);
        if (MainController.mainController.getConnection().isOk()) {
          MainController.mainController.getConnection()
              .sendToServer(
                  new ConnectMessage(MainController.mainController.getUser(),
                      MainController.mainController.getUser().getPlayerID()));
          MainController.mainController.getPlayButton().setDisable(true);
          MainController.mainController
              .changePane(MainController.mainController.getRightPane(), "/view/GameInfo.fxml");
        }
      }
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setContentText("You cannot host, because you are not logged in.");
      errorAlert.show();
    }
  }

  /**
   * a new gamesession will be opened and with it a new server with a specific port. many different
   * settings are safed.
   *
   * @author socho
   */
  private boolean hostGameSession() {
    this.port = Integer.parseInt(portNumberTextField.getText());
    if (this.port > 49151 && this.port < 50001) {
      gameSession = new GameSession(this.port);
      gameSession.setHost(MainController.mainController.getUser());
      gameSession.getServer().setServerHost(MainController.mainController.getUser());
      MainController.mainController.setGameSession(gameSession);
      MainController.mainController.setHosting(true);
      MainController.mainController.setServer(gameSession.getServer());
      return true;
    } else {
      portErrorLabel.setText("port number is not available.");
      portErrorLabel.setVisible(true);
      portNumberTextField.requestFocus();
      return false;
    }
  }

  /**
   * joinGameSession() will be called and a new pane will open up on the right side.
   *
   * @author socho
   */
  @FXML
  void join(ActionEvent event) throws IOException {
    if (MainController.mainController.getLoggedIn() == true) {
      if (joinGameSession()) {
        MainController.mainController
            .changePane(MainController.mainController.getRightPane(), "/view/GameInfo.fxml");
      }
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setContentText("You cannot join, because you are not logged in.");
      errorAlert.show();
    }
  }

  /**
   * client connects to the server with the server ip and port which the user entered before. if
   * connection is alright (see isOk() method in "Client.java"), a new Connect-Message will be sent
   * to the server. port field will be checked if it is empty and matches to specific regex. if not,
   * the right game info pane will not show up.
   *
   * @author socho
   */

  private boolean joinGameSession() throws IOException {
    this.port = Integer.parseInt(portField.getText());
    if (this.port > 49151 && this.port < 50001) {
      MainController.mainController.connectToServer("localhost", this.port);
      if (MainController.mainController.getConnection().isOk()) {
        MainController.mainController.getConnection()
            .sendToServer(
                new ConnectMessage(MainController.mainController.getUser(),
                    MainController.mainController.getUser().getPlayerID()));
        System.out
            .println(MainController.mainController.getUser().getUserName() + " is connected.");
        MainController.mainController.getPlayButton().setDisable(true);
        return true;
      } else {
        System.out
            .println(MainController.mainController.getUser().getUserName() + " cannot connect.");
        this.errorLabel.setText("Game session could not be found.");
        this.errorLabel.setVisible(true);
        return false;
      }
    } else {
      this.errorLabel.setText("Port is not ideal.");
      this.errorLabel.setVisible(true);
      return false;
    }
  }
}
