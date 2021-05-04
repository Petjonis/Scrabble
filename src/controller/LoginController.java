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

        System.out.println(MainController.mainController.getUserName());
        System.out.println(MainController.mainController.getLoggedIn());

        Alert confirmationAlert = new Alert(AlertType.INFORMATION);
        confirmationAlert.setContentText("Welcome " + MainController.mainController.getUserName() + "! \n" + "You are logged in!");
        confirmationAlert.show();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
      }else {
        /** user exists, but password is wrong.*/
        MainController.mainController.openNewWindow("/view/Login.fxml", "Login");
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        System.out.println("Login failed");
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setContentText("Sorry, but your password was wrong!" + "\n" + "Try again, please.");
        errorAlert.show();
        MainController.mainController.setLoggedIn(false);
      }
    }else {
      /** user does not exist and has to register first.*/
      MainController.mainController.openNewWindow("/view/Register.fxml", "Register");
      Stage stage = (Stage) loginButton.getScene().getWindow();
      stage.close();

      Alert errorAlert = new Alert (AlertType.ERROR);
      errorAlert.setContentText("Sorry, but '" + usernameField.getText() + "' does not exist in the database." + "\n" + "Please register first.");
      errorAlert.show();
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
