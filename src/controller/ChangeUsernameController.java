package controller;

import com.jfoenix.controls.JFXButton;
import db.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangeUsernameController implements Initializable {
  @FXML private JFXButton changeButton;

  @FXML private TextField newUsernameField;

  @FXML private Label errorLabel;

  @FXML private Button closeButton;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
    this.newUsernameField.requestFocus();
  }

  @FXML
  void change(ActionEvent event) {
    MainController.mainController.db = new Database();
    MainController.mainController.db.connect();
    if (!MainController.mainController.db.userExists(this.newUsernameField.getText())) {
      MainController.mainController.db.changeUsername(
          MainController.mainController.getUser().getUserName(), this.newUsernameField.getText());

      MainController.mainController.getUser().setUserName(this.newUsernameField.getText());
      ProfileController.profileController.setUsername(this.newUsernameField.getText());

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

  @FXML
  void close(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  public void userNameKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      changeButton.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      changeButton.requestFocus();
    }
  }

  public void changeKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.ENTER) change(new ActionEvent());
  }
}
