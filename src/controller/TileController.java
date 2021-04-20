package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class TileController extends StackPane {
    @FXML
    private StackPane tile;

    @FXML
    private Label letter;

    @FXML
    private Label value;

    public TileController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Tile.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public String getLetter() {
        return letter.getText();
    }

    public void setLetter(String letter) {
        this.letter.setText(letter);
    }

    public String getValue() {
        return value.getText();
    }

    public void setValue(String value) {
        this.value.setText(value);
    }

    public void setString(String s) {
        String[] split = s.split(",");
        this.setLetter(split[0]);
        this.setValue(split[1]);
    }

}
