package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class GameInfoController {
    @FXML
    private AnchorPane gameInfoPane;

    @FXML
    private JFXListView<String> playerList;

    @FXML
    private JFXListView<String> lastWordList;

    @FXML
    private JFXListView<String> chatList;

    @FXML
    private JFXTextArea sendText;

    @FXML
    private JFXButton sendButton;

    @FXML
    void send(ActionEvent event) {

    }
}
