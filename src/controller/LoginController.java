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
import model.HumanPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

  @FXML private JFXButton loginButton;

  @FXML private Button closeButton;

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML private Label errorLabel;

  @FXML private Hyperlink signupLink;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabel.setVisible(false);
    this.usernameField.requestFocus();
  }

  /**
   * when pressing "login" button, three different scenarios can occur. First, login is successful.
   * Second, password is wrong. Third, user does not exist.
   *
   * @author socho, fpetek
   */
  @FXML
  void login(ActionEvent event) throws IOException {
    MainController.mainController.db.connect();

    if (MainController.mainController.db.userExists(usernameField.getText())) {
      if (MainController.mainController.db.checkPassword(
          usernameField.getText(), passwordField.getText())) {
        /** login is successful. */
        System.out.println("Login successful!");

        MainController.mainController.setUser(new HumanPlayer(usernameField.getText()));
        MainController.mainController.setLoggedIn(true);

        MainController.mainController.getWelcomeLabel().setText(usernameField.getText());
        MainController.mainController.getWelcomeLabel().setVisible(true);
        MainController.mainController.getEditProfileIcon().setVisible(true);

        MainController.mainController.getLoginButton().setVisible(false);
        MainController.mainController.getSignupButton().setVisible(false);
        MainController.mainController.getLogoutButton().setVisible(true);
        MainController.mainController.getPlayButton().setDisable(false);
        MainController.mainController.getChangeUsernameButton().setVisible(false);
        MainController.mainController.getChangePasswordButton().setVisible(false);
        MainController.mainController.getDeleteProfileButton().setVisible(false);

        MainController.mainController.getGameCountLabel().setVisible(true);
        MainController.mainController.getWinCountLabel().setVisible(true);
        MainController.mainController.getLoseCountLabel().setVisible(true);
        MainController.mainController.getWinRateLabel().setVisible(true);
        MainController.mainController.getAvgPointsLabel().setVisible(true);

        MainController.mainController.getGameCount().setVisible(true);
        MainController.mainController.getWinCount().setVisible(true);
        MainController.mainController.getLoseCount().setVisible(true);
        MainController.mainController.getWinRate().setVisible(true);
        MainController.mainController.getAvgPoints().setVisible(true);

        /** Initializing account statistic values from database. */
        MainController.mainController.setGameCount(
            Integer.toString(
                MainController.mainController.db.getGames(
                    MainController.mainController.getUser().getUserName())));

        MainController.mainController.setWinCount(
            Integer.toString(
                MainController.mainController.db.getWins(
                    MainController.mainController.getUser().getUserName())));

        MainController.mainController.setLoseCount(
            Integer.toString(
                MainController.mainController.db.getLoses(
                    MainController.mainController.getUser().getUserName())));
        /** checks if wins are divided by 0. */
        if (MainController.mainController.db.getGames(
                MainController.mainController.getUser().getUserName())
            != 0) {
          MainController.mainController.setWinRate(
              String.format(
                      Locale.ENGLISH,
                      "%3.1f",
                      ((double)
                                  (MainController.mainController.db.getWins(
                                      MainController.mainController.getUser().getUserName()))
                              / (double)
                                  MainController.mainController.db.getGames(
                                      MainController.mainController.getUser().getUserName()))
                          * 100)
                  + " %");
        } else {
          MainController.mainController.setWinRate("0 %");
        }

        MainController.mainController.setAvgPoints(
            Integer.toString(
                MainController.mainController.db.getScore(
                    MainController.mainController.getUser().getUserName())));
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

      } else {
        /** user exists, but password is wrong. */
        System.out.println("Login failed, reason: wrong password.");
        this.errorLabel.setText("Wrong Password! Try again, please.");
        this.errorLabel.setVisible(true);
        this.passwordField.requestFocus();
        MainController.mainController.setLoggedIn(false);
      }
    } else {
      /** user does not exist and has to register first. */
      System.out.println("Login failed, reason: username does not exist in the database.");
      this.errorLabel.setText(
          usernameField.getText() + "' does not exist in the database. Please register first.");
      this.errorLabel.setVisible(true);
      this.usernameField.clear();
      this.passwordField.clear();
      MainController.mainController.setLoggedIn(false);
    }
    MainController.mainController.db.disconnect();
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

  /** Methods that user can use tab or enter to navigate through textfields. */
  public void userNameKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      passwordField.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      passwordField.requestFocus();
    }
  }

  /** when "Login" button is selected, it can be pressed with an "ENTER" key. */
  public void loginKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      login(new ActionEvent());
    }
  }

  /**
   * after entering password in the password-text field and pressing "TAB" or "ENTER" key, login
   * button will be selected.
   */
  public void passwordKeyPressed(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode() == KeyCode.TAB) {
      loginButton.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      login(new ActionEvent());
    }
  }
}
