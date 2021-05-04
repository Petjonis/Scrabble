package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import messages.ConnectMessage;

public class PlayOnlineController {

  private int port;


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

  /**
   * if "host" button is pressed, server runs and waits for clients to connect.
   *
   * @author socho
   */
  @FXML
  void host(ActionEvent event) throws IOException {
    if (MainController.mainController.getLoggedIn() == true) {
      System.out.println(MainController.mainController.getUserName() + ", you are the Host.");
      MainController.mainController
          .changePane(MainController.mainController.getRightPane(), "/view/GameInfo.fxml");

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
    }
  }

  /**
   * if "join" button is pressed, client connects to the server and port which he entered before and
   * sends a ConnectMessage to the server.
   *
   * @author socho
   */
  @FXML
  void join(ActionEvent event) throws IOException {
    MainController.mainController
        .connectToServer(ipField.getText(), Integer.parseInt(portField.getText()));
    if (MainController.mainController.getConnection().isOk()) {
      MainController.mainController.getConnection()
          .sendToServer(new ConnectMessage(MainController.mainController.getUserName()));
      System.out.println(MainController.mainController.getUserName() + " is connected.");
    } else {
      System.out.println(MainController.mainController.getUserName() + " cannot connect.");
    }
  }

}
