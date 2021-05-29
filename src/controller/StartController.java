package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class StartController {

  @FXML private JFXButton playOnlineButton;

  @FXML
  void playOnline(ActionEvent event) throws IOException {
    MainController.mainController.openPlay(new ActionEvent());
  }
}
