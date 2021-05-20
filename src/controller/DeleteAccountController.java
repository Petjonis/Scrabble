package controller;

import com.jfoenix.controls.JFXButton;
import db.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteAccountController implements Initializable {

  @FXML private JFXButton deleteButton;

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML private Label errorLabel;

  @FXML private Button closeButton;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
    this.usernameField.requestFocus();
  }

  @FXML
  void delete(ActionEvent event) throws IOException {
    MainController.mainController.db = new Database();
    MainController.mainController.db.connect();

    if (MainController.mainController.db.userExists(usernameField.getText())) {
      if (MainController.mainController.db.checkPassword(
          usernameField.getText(), passwordField.getText())) {
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
        MainController.mainController.logout(new ActionEvent());
        MainController.mainController.db.deleteUser(usernameField.getText());

        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setContentText("Successfully deleted Account!");
        informationAlert.show();
      } else {
        /** user exists, but password is wrong. */
        System.out.println("Deletion failed, reason: wrong password.");
        this.errorLabel.setText("Wrong Password! Try again, please.");
        this.errorLabel.setVisible(true);
        this.usernameField.requestFocus();
        }
    }else {
      /** user does not exist and has to register first.*/
      System.out.println("Deletion failed, reason: username does not exist in the database.");
      this.errorLabel.setText(
              usernameField.getText() + "' does not exist in the database. Please try again!.");
      this.errorLabel.setVisible(true);
      this.usernameField.clear();
      this.passwordField.clear();
      this.usernameField.requestFocus();
    }

    MainController.mainController.db.disconnect();
  }

  @FXML
  void close(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  public void userNameKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB){
      passwordField.requestFocus();
    }else if (keyEvent.getCode() == KeyCode.ENTER){
      passwordField.requestFocus();
    }
  }

  public void passwordKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB){
      deleteButton.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER){
      deleteButton.requestFocus();
    }
  }

  public void deleteKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.ENTER)
      delete(new ActionEvent());
  }


}