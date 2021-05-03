package controller;

import com.jfoenix.controls.JFXButton;
import java.util.Locale;
import db.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

  /**
   * after pressing the "create Account" button, user will be notified if registration was
   * successful or not
   *
   * @author socho
   */
  @FXML
  void createAccount(ActionEvent event) {
    Locale.setDefault(new Locale("en", "English"));

    Database db = new Database();
    db.connect();
    db.createUserTable();

    /** when registration was successful, system shows login page and notifies user */
    if (!db.userExists(usernameField.getText())) {
      db.addUser(usernameField.getText(), passwordField.getText());

      MainController mc1 = new MainController();
      mc1.openNewWindow("/view/Login.fxml", "Login");
      Stage stage = (Stage) createButton.getScene().getWindow();
      stage.close();

      Alert alert1 = new Alert(AlertType.INFORMATION);
      alert1.setContentText("User added to the database.");
      alert1.show();

      /** when registration failed / user already exists in the database, system will show the register page again and notifies the user.*/
    } else {
      MainController mc1 = new MainController();
      mc1.openNewWindow("/view/Register.fxml", "Register");
      Stage stage = (Stage) createButton.getScene().getWindow();
      stage.close();

      Alert alert2 = new Alert(AlertType.ERROR);
      alert2.setContentText("User already exists");
      alert2.show();
    }
    db.disconnect();
  }
}
