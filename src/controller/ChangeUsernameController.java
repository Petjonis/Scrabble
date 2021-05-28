/**
 * This controller class initializes a new window to change a username with all needed methods
 * listeners and methods.
 *
 * @author fpetek
 * @version 1.0
 */

package controller;

import com.jfoenix.controls.JFXButton;
import db.Database;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ChangeUsernameController implements Initializable {

  @FXML private JFXButton changeButton;

  @FXML private TextField newUsernameField;

  @FXML private Label errorLabel;

  @FXML private Button closeButton;

  /**
   * Initializes "ChangeUsername" view.
   *
   * @param url Gets called automatically.
   * @param resourceBundle Gets called automatically.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
    this.newUsernameField.requestFocus();
  }

  /**
   * Method to change database entry of a users username.
   *
   * @param event Listens on Change-Button.
   */
  @FXML
  void changeUsername(ActionEvent event) {
    MainController.mainController.db.connect();
    if (!MainController.mainController.db.userExists(this.newUsernameField.getText())) {
      MainController.mainController.db.changeUsername(
          MainController.mainController.getUser().getUserName(), this.newUsernameField.getText());

      MainController.mainController.getUser().setUserName(this.newUsernameField.getText());
      MainController.mainController.setWelcomeLabel(this.newUsernameField.getText());

      Stage stage = (Stage) changeButton.getScene().getWindow();
      stage.close();
    } else {
      /** Username already exists. */
      this.errorLabel.setText("Username not available!");
      this.errorLabel.setVisible(true);
      this.newUsernameField.clear();
      this.newUsernameField.requestFocus();
    }

    MainController.mainController.db.disconnect();
  }

  /**
   * Method to close ChangeUsername Window.
   *
   * @param event Listens on Close-Button.
   */
  @FXML
  void close(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  /** Method that user can use tab or enter to navigate through textfields. */
  public void userNameKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      changeButton.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      changeButton.requestFocus();
    }
  }

  /** Method that user can use enter to activate changeButton. */
  public void changeKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      changeUsername(new ActionEvent());
    }
  }
}
