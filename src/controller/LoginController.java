package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML
    void login(ActionEvent event) {

    }

    @FXML
    void signup(ActionEvent event) {

    }

    @FXML
    void close(ActionEvent event){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
