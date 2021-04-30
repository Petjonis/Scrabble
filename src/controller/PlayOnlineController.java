package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import network.Client;
import network.Server;
import settings.ServerSettings;


public class PlayOnlineController {

  private Server server;
  private Client connection;

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
   * method for connecting to the server.
   *
   * @author socho
   */
  public void connectToServer(String host, int port) throws IOException {
    this.connection = new Client(host, ServerSettings.port);
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
   * if "host" button is pressed, server runs and waits for clients to connect.
   *
   * @author socho
   */
  @FXML
  void host(ActionEvent event) throws IOException {
    MainController.mainController
        .changePane(MainController.mainController.getRightPane(), "/view/GameInfo.fxml");

    server = new Server();
    Runnable r = new Runnable() {
      @Override
      public void run() {
        try {
          server.listen();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
      }
    };
    new Thread(r).start();
  }

  /**
   * if "join" button is pressed, client connects to the server and port which he entered before.
   *
   * @author socho
   */
  @FXML
  void join(ActionEvent event) throws IOException {
    connectToServer(ipField.getText(), Integer.parseInt(portField.getText()));
    if (connection.isOk()) {
      System.out.println("Client is connected.");
    } else {
      System.out.println("Client cannot connect.");
    }
  }

}
