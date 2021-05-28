package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class StartController {

  @FXML private JFXButton playOnlineButton;

  @FXML private JFXButton playTutorialButton;

  @FXML private Label gamesPlayedLabel;

  @FXML private Label currentPlayingLabel;

  @FXML
  void playOnline(ActionEvent event) throws IOException {
    MainController.mainController.openPlay(new ActionEvent());
  }

  @FXML
  void playTutorial(ActionEvent event) throws IOException {
    MainController.mainController.openLearn(new ActionEvent());
  }
}
