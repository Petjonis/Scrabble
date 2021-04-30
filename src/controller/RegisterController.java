package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class RegisterController {

  @FXML
  private JFXButton createButton;

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField confirmField;

  @FXML
  private Button uploadButton;

  @FXML
  private ImageView previewImage;

  @FXML
  private Button closeButton;

  @FXML
  void chooseFile(ActionEvent event) {

  }

  @FXML
  void close(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  void createAccount(ActionEvent event) {

  }
}
