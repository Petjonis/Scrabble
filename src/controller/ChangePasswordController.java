/**
 * This controller class initializes a new window to change a users password with all needed
 * listeners and methods to change it and makes sure everything goes fine.
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ChangePasswordController implements Initializable {

  @FXML private JFXButton changeButton;

  @FXML private PasswordField currentPasswordField;

  @FXML private PasswordField newPasswordField;

  @FXML private PasswordField confirmPasswordField;

  @FXML private Label errorLabel;

  @FXML private Button closeButton;

  /**
   * Initializes "ChangePassword" view.
   *
   * @param url Gets called automatically.
   * @param resourceBundle Gets called automatically.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
    this.currentPasswordField.requestFocus();
  }

  /**
   * Method to change database entry of a users password.
   *
   * @param event Listens on Change-Button.
   */
  @FXML
  void changePassword(ActionEvent event) {
    MainController.mainController.db = new Database();
    MainController.mainController.db.connect();

    if (MainController.mainController.db.checkPassword(
        MainController.mainController.getUser().getUserName(), currentPasswordField.getText())) {
      if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
        MainController.mainController.db.changePassword(
            MainController.mainController.getUser().getUserName(), newPasswordField.getText());

        Stage stage = (Stage) changeButton.getScene().getWindow();
        stage.close();

        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setContentText("Changed Password successfully!");
        informationAlert.show();

      } else {
        /** New password and Confirm password doesn't match */
        this.errorLabel.setText("New Password and Confirm new password doesn't match!");
        this.errorLabel.setVisible(true);
        this.currentPasswordField.clear();
        this.newPasswordField.clear();
        this.confirmPasswordField.clear();
        this.currentPasswordField.requestFocus();
      }
    } else {
      /** Current password wrong */
      this.errorLabel.setText("Current password wrong!");
      this.errorLabel.setVisible(true);
      this.currentPasswordField.clear();
      this.newPasswordField.clear();
      this.confirmPasswordField.clear();
      this.currentPasswordField.requestFocus();
    }

    MainController.mainController.db.disconnect();
  }

  /**
   * Method to close ChangePassword Window.
   *
   * @param event Listens on Close-Button.
   */
  @FXML
  void close(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  /** Method that user can use tab or enter to navigate through textfields. */
  public void currentPasswordKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      newPasswordField.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      newPasswordField.requestFocus();
    }
  }

  /** Method that user can use tab or enter to navigate through textfields. */
  public void newPasswordKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      confirmPasswordField.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      confirmPasswordField.requestFocus();
    }
  }
  /** Method that user can use tab or enter to navigate through textfields. */
  public void confirmPasswordKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      changeButton.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      changeButton.requestFocus();
    }
  }
  /** Method that user can use enter to activate changeButton. */
  public void changeKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      changePassword(new ActionEvent());
    }
  }
}
