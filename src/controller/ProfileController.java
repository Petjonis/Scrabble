package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import db.Database;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.GameSession;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

  public static ProfileController profileController;

  @FXML private JFXButton deleteProfileButton;

  @FXML private JFXButton changeUsernameButton;

  @FXML private JFXButton deleteProfileButt;

  @FXML private JFXButton changePasswordButton;

  @FXML private Label username;

  @FXML private Label gameCountLabel;

  @FXML private Label winCountLabel;

  @FXML private Label loseCountLabel;

  @FXML private Label winRateLabel;

  @FXML private Label avgPointsLabel;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    profileController = this;

    MainController.mainController.db = new Database();
    MainController.mainController.db.connect();

    System.out.println(MainController.mainController.getUser().getUserName());

    this.username.setText(MainController.mainController.getUser().getUserName());
    this.gameCountLabel.setText(
        Integer.toString(
            MainController.mainController.db.getGames(
                MainController.mainController.getUser().getUserName())));
    this.winCountLabel.setText(
        Integer.toString(
            MainController.mainController.db.getWins(
                MainController.mainController.getUser().getUserName())));
    this.loseCountLabel.setText(
        Integer.toString(
            MainController.mainController.db.getLoses(
                MainController.mainController.getUser().getUserName())));

    this.winRateLabel.setText("0 %");
    if (MainController.mainController.db.getGames(
            MainController.mainController.getUser().getUserName())
        != 0) {
      this.winRateLabel.setText(
          Double.toString(
                  (double)
                          (MainController.mainController.db.getWins(
                              MainController.mainController.getUser().getUserName()))
                      / (double)
                          (MainController.mainController.db.getGames(
                              MainController.mainController.getUser().getUserName())))
              + " %");
    }

    this.avgPointsLabel.setText(
        Integer.toString(
            MainController.mainController.db.getScore(
                MainController.mainController.getUser().getUserName())));

    MainController.mainController.db.disconnect();
  }

  @FXML
  void changeUsername(ActionEvent event) {
    MainController.mainController.openNewWindow("/view/ChangeUsername.fxml", "ChangeUsername");
  }

  @FXML
  void changePassword(ActionEvent event) {
    MainController.mainController.openNewWindow("/view/ChangePassword.fxml", "ChangePassword");
  }

  @FXML
  void deleteAccount(ActionEvent event) {
    MainController.mainController.openNewWindow("/view/DeleteAccount.fxml", "ChangeAccount");
  }

  public void setUsername(String username) {
    this.username.setText(username);
  }
}
