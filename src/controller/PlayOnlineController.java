package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import messages.ConnectMessage;
import model.GameSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayOnlineController implements Initializable {

  private String ipAddress;
  private int port;
  private GameSession gameSession;

  @FXML private TabPane playTabPane;
  @FXML private Label descriptionLabel;
  @FXML private TextField portNumberTextField;
  @FXML private Label portErrorLabel;
  @FXML private JFXButton hostButton;
  @FXML private TextField ipField;
  @FXML private TextField portField;
  @FXML private JFXButton joinButton;
  @FXML private Label errorLabel;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
    this.portErrorLabel.setVisible(false);
    this.portNumberTextField.setText("49152");
    this.ipField.setText("localhost");
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
        System.out.println(
            MainController.mainController.getUser().getUserName() + ", you are the Host.");

        Runnable r =
            new Runnable() {
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
          MainController.mainController
              .getConnection()
              .sendToServer(new ConnectMessage(MainController.mainController.getUser()));
          // MainController.mainController.getPlayButton().setDisable(true);
          MainController.mainController.changePane(
              MainController.mainController.getRightPane(), "/view/GameInfo.fxml");
        }
      }
    } else {
      MainController.mainController.openNewWindow("/view/Login.fxml", "Login");
    }
  }

  /**
   * joinGameSession() will be called and a new pane will open up on the right panel.
   *
   * @author socho
   */
  @FXML
  void join(ActionEvent event) throws IOException {
    if (MainController.mainController.getLoggedIn() == true) {
      if (joinGameSession()) {
        MainController.mainController.changePane(
            MainController.mainController.getRightPane(), "/view/GameInfo.fxml");
      }
    } else {
      MainController.mainController.openNewWindow("/view/Login.fxml", "Login");
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
   * client connects to the server with the server ip and port which the user entered before. if
   * connection is alright (see isOk() method in "Client.java"), a new Connect-Message will be sent
   * to the server. port field will be checked if it is empty and matches to specific regex. if not,
   * the right game info pane will not show up.
   *
   * @author socho
   */
  private boolean joinGameSession() throws IOException {
    ipAddress = ipField.getText();
    if (!ipAddress.isEmpty()) {
      port = Integer.parseInt(portField.getText());
    } else {
      errorLabel.setText("ip is not available.");
      errorLabel.setVisible(true);
      return false;
    }
    if (port > 49151 && port < 50001) {
      MainController.mainController.connectToServer(ipAddress, port);
      if (MainController.mainController.getConnection().isOk()) {
        MainController.mainController
            .getConnection()
            .sendToServer(new ConnectMessage(MainController.mainController.getUser()));
        System.out.println(
            MainController.mainController.getUser().getUserName() + " is connected.");
        // MainController.mainController.getPlayButton().setDisable(true);
        return true;
      } else {
        System.out.println(
            MainController.mainController.getUser().getUserName() + " cannot connect.");
        this.errorLabel.setText("Game session could not be found.");
        this.errorLabel.setVisible(true);
        return false;
      }
    } else {
      this.errorLabel.setText("Port is not available.");
      this.errorLabel.setVisible(true);
      return false;
    }
  }

  /**
   * when entering in the "ip" text field by pressing "TAB" key "port" text field will be focused.
   *
   * @author socho
   */
  public void ipFieldKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      portField.requestFocus();
    }
  }

  /**
   * when entering in the "port" text field by pressing "TAB" key "Join" button will be highlighted
   * By pressing "ENTER" key, you will join.
   *
   * @author socho
   */
  public void hostPortKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.TAB) {
      joinButton.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      join(new ActionEvent());
    }
  }

  public void joinKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      join(new ActionEvent());
    }
  }
}
