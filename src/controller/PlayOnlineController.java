package controller;

import com.jfoenix.controls.JFXButton;
import com.sun.tools.javac.Main;
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
  private GameSession gameSession ;


  @FXML
  private TabPane playTabPane;

  @FXML
  private JFXButton fileChooserButton;

  @FXML
  private Label filePathLabel;

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
   this.ipField.setText("localhost");
  }

  /**
   * "Host"-button can only be pressed, when player is logged in.
   * System recognizes host and sets "Host"-token to the user.
   * Host will also be on the list, which contains all players who are in the game session.
   * System changes the view and server runs.
   *
   * @author socho
   */
  @FXML
  void host(ActionEvent event) throws IOException {
    if (MainController.mainController.getLoggedIn() == true) {
      System.out.println(MainController.mainController.getUser().getUserName() + ", you are the Host.");
      hostGameSession();


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

      MainController.mainController.connectToServer("localhost", 8080);
      if (MainController.mainController.getConnection().isOk()) {
        MainController.mainController.getConnection()
            .sendToServer(new ConnectMessage(MainController.mainController.getUser().getUserName()));
        MainController.mainController
            .changePane(MainController.mainController.getRightPane(), "/view/GameInfo.fxml");
      }
    }else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setContentText("You cannot host, because you are not logged in.");
      errorAlert.show();
    }
  }

  private void hostGameSession(){
    gameSession = new GameSession(8080, MainController.mainController.getUser());
    MainController.mainController.setGameSession(gameSession);
    MainController.mainController.setHosting(true);
    MainController.mainController.setServer(gameSession.getServer());
    MainController.mainController.getUser().setActiveSession(gameSession);
  }

  /**
   * if "join" button is pressed,
   * client connects to the server and port which he entered before, if connection is okay.
   * Client sends a ConnectMessage to the server. He will be registered in the game session.
   * System prints out the username who joined.
   * Client will join the game session and see the other players and written chat history.
   *
   * @author socho
   */
  @FXML
  void join(ActionEvent event) throws IOException {
    joinGameSession();
    MainController.mainController
        .changePane(MainController.mainController.getRightPane(), "/view/GameInfo.fxml");

  }

  private void joinGameSession() throws IOException {
    port = Integer.parseInt(portField.getText());
    MainController.mainController.connectToServer(ipField.getText(), port);
    if (MainController.mainController.getConnection().isOk()) {
      MainController.mainController.getConnection()
          .sendToServer(new ConnectMessage(MainController.mainController.getUser().getUserName()));
      System.out.println(MainController.mainController.getUser().getUserName() + " is connected.");
    } else {
      System.out.println(MainController.mainController.getUser().getUserName() + " cannot connect.");
      this.errorLabel.setText("Game session could not be found.");
      this.errorLabel.setVisible(true);
    }
  }
}
