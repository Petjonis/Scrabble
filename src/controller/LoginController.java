package controller;

import com.jfoenix.controls.JFXButton;
import com.sun.tools.javac.Main;
import db.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginController {

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

  /** when pressing "login" button, three different scenarios can occur. First, login is successful. Second, password is wrong. Third, user does not exist.
   * @author socho
   */
  @FXML
  void login(ActionEvent event) {
    Database db = new Database ();
    db.connect();

    if(db.userExists(usernameField.getText())){
      if (db.checkPassword(usernameField.getText(), passwordField.getText())){
        /** login is successful.*/
        System.out.println("Login successful!");

        MainController.mainController.setUserName(usernameField.getText());
        MainController.mainController.setLoggedIn(true);

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
      }else {
        /** user exists, but password is wrong.*/
        System.out.println("Login failed, reason: wrong password.");
        this.errorLabel.setText("Wrong Password! Try again, please.");
        this.passwordField.getSelection();
        MainController.mainController.setLoggedIn(false);
      }
    }else {
      /** user does not exist and has to register first.*/
      System.out.println("Login failed, reason: username does not exist in the database.");
      this.errorLabel.setText( usernameField.getText() + "' does not exist in the database. Please register first.");
      this.usernameField.clear();
      this.passwordField.clear();
      MainController.mainController.setLoggedIn(false);
    }
  }

  /**
   * When pressing "signUp" Hyperlink, user will be redirected to the signup page. And login window
   * closes.
   * @author socho
   */
  @FXML
  void signup(ActionEvent event) {
    MainController.mainController.openNewWindow("/view/Register.fxml", "Register");
    Stage stage = (Stage) signupLink.getScene().getWindow();
    stage.close();
  }

  @FXML
  void close(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

}
