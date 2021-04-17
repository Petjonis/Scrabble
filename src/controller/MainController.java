package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

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
    private FlowPane startPane;

    @FXML
    StackPane centerPane;

    @FXML
    StackPane rightPane;

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void openLearn(ActionEvent event) {

    }

    @FXML
    void openPlay(ActionEvent event) throws IOException {

        centerPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/GameBoard.fxml"));
        Parent gBoard = (Parent) fxmlLoader.load();
        centerPane.getChildren().add(gBoard);

        rightPane.getChildren().clear();
        fxmlLoader = new FXMLLoader(getClass().getResource("/view/PlayOnline.fxml"));
        Parent playTab = (Parent) fxmlLoader.load();
        rightPane.getChildren().add(playTab);
    }

    @FXML
    void openRulebook(ActionEvent event) {

    }

    @FXML
    void signup(ActionEvent event) {
        openNewWindow("/view/Register.fxml", "Register");
    }


    @FXML
    void login(ActionEvent event) {
        openNewWindow("/view/Login.fxml", "Login");
    }

    public void openNewWindow(String filename, String title){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle(title);
            Scene scene = new Scene(root1);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
