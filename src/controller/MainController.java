package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private JFXButton playButton;

    @FXML
    private JFXButton learnButton;

    @FXML
    private JFXButton rulebookButton;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton signupButton;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane registerPane;

    @FXML
    private StackPane loginPane;

    @FXML
    private FlowPane startPane;

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void openLearn(ActionEvent event) {

    }

    @FXML
    void openPlay(ActionEvent event) {
        startPane.toFront();
    }

    @FXML
    void openRulebook(ActionEvent event) {

    }

    @FXML
    void signup(ActionEvent event) {
        loginPane.toBack();
        registerPane.toFront();
    }


    @FXML
    void login(ActionEvent event) {
        registerPane.toBack();
        loginPane.toFront();
    }
}
