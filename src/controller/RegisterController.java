package controller;

import com.jfoenix.controls.JFXButton;
import db.Database;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class RegisterController implements Initializable {

  @FXML
  private Label userError;
  @FXML
  private Label passwordError;
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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.userError.setVisible(false);
    this.passwordError.setVisible(false);
  }


  /**
   * after pressing the "create Account" button, three scenarios can occur: first, registration is
   * successful. second username already exists in the database. third, password does not match.
   *
   * @author socho
   */
  @FXML
  void createAccount(ActionEvent event) {
    Locale.setDefault(new Locale("en", "English"));

    Database db = new Database();
    db.connect();
    db.createUserTable();
    this.userError.setVisible(false);
    this.passwordError.setVisible(false);

    /** when registration was successful, user will be added to the database and system shows login page. */
    if (!db.userExists(usernameField.getText()) && confirmField.getText()
        .equals(passwordField.getText())) {
      db.addUser(usernameField.getText(), passwordField.getText());

      MainController.mainController.openNewWindow("/view/Login.fxml", "Login");
      Stage stage = (Stage) createButton.getScene().getWindow();
      stage.close();

      /** when registration failed / password does not match.*/
    } else if (!db.userExists(usernameField.getText()) && !confirmField.getText()
        .equals(passwordField.getText())) {
      this.passwordError.setVisible(true);
      this.confirmField.clear();
      /** when registration failed / userName already exists in the database.*/
    } else {
      this.userError.setVisible(true);
      this.usernameField.clear();
      this.passwordField.clear();
      this.confirmField.clear();
      this.previewImage.setImage(null);
    }
    db.disconnect();
  }

  /**
   * Sign up can be pressed with the "ENTER" key.
   */
  public void signUpKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      createAccount(new ActionEvent());
    }
  }

  /**
   * when username text field is on focus, password text field can be selected by pressing "TAB" or
   * "ENTER" key.
   */
  public void userNameKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      passwordField.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      passwordField.requestFocus();
    }
  }

  /**
   * when password text field is on focus, confirm text field can be selected by pressing "TAB" or
   * "ENTER" key.
   */
  public void passwordKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      confirmField.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      confirmField.requestFocus();
    }
  }

  /**
   * when confirm text field is on focus, image viewer can be selected by pressing "TAB" or "ENTER"
   * key.
   */
  public void confirmKeyPassword(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.TAB) {
      previewImage.requestFocus();
    } else if (keyEvent.getCode() == KeyCode.ENTER) {
      previewImage.requestFocus();
    }
  }

  /**
   * when image viewer is on focus, image viewer can be used by pressing "ENTER" key and sign up
   * button can be selected by pressing "TAB" key.
   */
  public void avatarKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      chooseFile(new ActionEvent());
    } else if (keyEvent.getCode() == KeyCode.TAB) {
      createButton.requestFocus();
    }
  }
}
