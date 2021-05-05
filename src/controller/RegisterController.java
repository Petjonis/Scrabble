package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Locale;
import db.Database;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
   * after pressing the "create Account" button, three scenarios can occur: first, registration is successful. second username already exists in the database. third, password does not match.
   *
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

    /** when registration was successful, system shows login page. */
    if (!db.userExists(usernameField.getText()) && confirmField.getText().equals(passwordField.getText())) {
      db.addUser(usernameField.getText(), passwordField.getText());

      MainController.mainController.openNewWindow("/view/Login.fxml", "Login");
      Stage stage = (Stage) createButton.getScene().getWindow();
      stage.close();

      /** when registration failed / password does not match.*/
    } else if (!db.userExists(usernameField.getText()) && !confirmField.getText().equals(passwordField.getText())) {
      this.passwordError.setVisible(true);
      this.confirmField.clear();
      /** when registration failed / userName already exists in the database.*/
    }else {
      this.userError.setVisible(true);
      this.usernameField.clear();
      this.passwordField.clear();
      this.confirmField.clear();
      this.previewImage.setImage(null);
    }
    db.disconnect();
  }


}
