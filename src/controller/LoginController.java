package controller;

import com.jfoenix.controls.JFXButton;
import com.sun.tools.javac.Main;
import db.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
private String userName ;

  @FXML
  private JFXButton loginButton;

  @FXML
  private Button closeButton;

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Label errorLabel;

  @FXML
  private Hyperlink signupLink;

  @FXML
  void login(ActionEvent event) {

  }

  /**
   * When pressing "signUp" Hyperlink, user will be redirected to the signup page. And login window
   * closes.
   */
  @FXML
  void signup(ActionEvent event) {
    MainController mc1 = new MainController();
    mc1.openNewWindow("/view/Register.fxml", "Register");
    Stage stage = (Stage) signupLink.getScene().getWindow();
    stage.close();
  }

  @FXML
  void close(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  public String getUserName(){
    return this.userName;
  }
}
