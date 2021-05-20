package controller;

import com.jfoenix.controls.JFXButton;
import db.Database;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

  @FXML private JFXButton changeButton;

  @FXML private PasswordField currentPasswordField;

  @FXML private PasswordField newPasswordField;

  @FXML private PasswordField confirmPasswordField;

  @FXML private Label errorLabel;

  @FXML private Button closeButton;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
    this.currentPasswordField.requestFocus();
  }

  @FXML
  void change(ActionEvent event) {
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

      } else{
        /** New password and Confirm password doesn't match*/
        this.errorLabel.setText("New Password and Confirm new password doesn't match!");
        this.errorLabel.setVisible(true);
        this.currentPasswordField.clear();
        this.newPasswordField.clear();
        this.confirmPasswordField.clear();
        this.currentPasswordField.requestFocus();
      }
    }else{
      /** Current password wrong*/
      this.errorLabel.setText("Current password wrong!");
      this.errorLabel.setVisible(true);
      this.currentPasswordField.clear();
      this.newPasswordField.clear();
      this.confirmPasswordField.clear();
      this.currentPasswordField.requestFocus();
    }

    MainController.mainController.db.disconnect();
  }

  @FXML
  void close(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  public void currentPasswordKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB){
      newPasswordField.requestFocus();
    }else if (keyEvent.getCode() == KeyCode.ENTER){
      newPasswordField.requestFocus();
    }
  }

  public void newPasswordKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB){
      confirmPasswordField.requestFocus();
    }else if (keyEvent.getCode() == KeyCode.ENTER){
      confirmPasswordField.requestFocus();
    }
  }
  public void confirmPasswordKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB){
      changeButton.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER){
      changeButton.requestFocus();
    }
  }

  public void changeKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.ENTER)
      change(new ActionEvent());
  }
}
