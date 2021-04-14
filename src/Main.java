import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stg;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        //primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        primaryStage.setTitle("Scrabble");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }
}
