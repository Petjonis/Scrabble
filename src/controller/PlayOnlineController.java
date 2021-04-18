package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.io.IOException;


public class PlayOnlineController {
    @FXML
    private TabPane playTabPane;

    @FXML
    private JFXButton fileChooserButton;

    @FXML
    private Label filePathLabel;

    @FXML
    private JFXButton hostButton;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private JFXButton joinButton;

    @FXML
    private Label errorLabel;

    @FXML
    void chooseFile(ActionEvent event) {

    }

    @FXML
    void host(ActionEvent event) throws IOException {
        MainController.mainController.changePane(MainController.mainController.getRightPane(), "/view/GameInfo.fxml");
    }

    @FXML
    void join(ActionEvent event) {

    }

}