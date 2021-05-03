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
private String userName;
private boolean loggedIn = false;

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
        this.userName = usernameField.getText();
        System.out.println("Login successful!");
        this.loggedIn = true ;

        Alert confirmationAlert = new Alert(AlertType.INFORMATION);
        confirmationAlert.setContentText("Welcome " + this.userName + "! \n" + "You are logged in!");
        confirmationAlert.show();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
      }else {
        /** user exists, but password is wrong.*/
        MainController mc1 = new MainController();
        mc1.openNewWindow("/view/Login.fxml", "Login");
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        System.out.println("Login failed");
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setContentText("Sorry, but your password was wrong!" + "\n" + "Try again, please.");
        errorAlert.show();
        this.loggedIn = false;
      }
    }else {
      /** user does not exist and has to register first.*/
      MainController mc1 = new MainController();
      mc1.openNewWindow("/view/Register.fxml", "Register");
      Stage stage = (Stage) loginButton.getScene().getWindow();
      stage.close();

      Alert errorAlert = new Alert (AlertType.ERROR);
      errorAlert.setContentText("Sorry, but '" + usernameField.getText() + "' does not exist in the database." + "\n" + "Please register first.");
      errorAlert.show();
      this.loggedIn = false;
    }
  }

  /**
   * When pressing "signUp" Hyperlink, user will be redirected to the signup page. And login window
   * closes.
   * @author socho
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

  public boolean getLoggedIn(){
    return this.loggedIn;
  }
}
