package controller;

import com.jfoenix.controls.JFXButton;
import com.sun.tools.javac.Main;
import db.Database;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.HumanPlayer;

public class LoginController implements Initializable {

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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
  }

  /**
   * when pressing "login" button, three different scenarios can occur. First, login is successful.
   * Second, password is wrong. Third, user does not exist.
   *
   * @author socho
   */
  @FXML
  void login(ActionEvent event) throws IOException {
    Database db = new Database();
    db.connect();

    if (db.userExists(usernameField.getText())) {
      if (db.checkPassword(usernameField.getText(), passwordField.getText())) {
        /** login is successful.*/
        System.out.println("Login successful!");

        MainController.mainController.setUser(new HumanPlayer());
        MainController.mainController.getUser().setUserName(usernameField.getText());
        MainController.mainController.setLoggedIn(true);

        MainController.mainController.getLoginButton().setVisible(false);
        MainController.mainController.getSignupButton().setVisible(false);
        MainController.mainController.getLogoutButton().setVisible(true);

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

      } else {
        /** user exists, but password is wrong.*/
        System.out.println("Login failed, reason: wrong password.");
        this.errorLabel.setText("Wrong Password! Try again, please.");
        this.errorLabel.setVisible(true);
        this.passwordField.getSelection();
        MainController.mainController.setLoggedIn(false);
      }
    } else {
      /** user does not exist and has to register first.*/
      System.out.println("Login failed, reason: username does not exist in the database.");
      this.errorLabel.setText(
          usernameField.getText() + "' does not exist in the database. Please register first.");
      this.errorLabel.setVisible(true);
      this.usernameField.clear();
      this.passwordField.clear();
      MainController.mainController.setLoggedIn(false);
    }
  }

  /**
   * When pressing "signUp" Hyperlink, user will be redirected to the signup page. And login window
   * closes.
   *
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
