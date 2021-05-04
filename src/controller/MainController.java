package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;
import network.Client;
import network.Server;
import settings.ServerSettings;

public class MainController implements Initializable {
    static MainController mainController;
    private String userName;
    private boolean loggedIn;
    private Server server = new Server();
    private Client connection ;

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
    private StackPane centerPane;

    @FXML
    private StackPane rightPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainController = this;
    }

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void openLearn(ActionEvent event) {

    }

    @FXML
    void openPlay(ActionEvent event) throws IOException {
        System.out.println(mainController.getUserName());
        System.out.println(mainController.getLoggedIn());
        changePane(centerPane, "/view/GameBoard.fxml");
        changePane(rightPane, "/view/PlayOnline.fxml");
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

    public void openNewWindow(String filename, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));
            Parent root1 = fxmlLoader.load();
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

    public void changePane(StackPane pane, String fxmlPath) throws IOException {
        pane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent parent = fxmlLoader.load();
        pane.getChildren().add(parent);
    }

    /**
     * method for connecting to the server.
     *
     * @author socho
     */
    public void connectToServer(String host, int port) throws IOException {
        this.connection = new Client(host, port);
        if (this.connection.isOk()) {
            this.connection.start();
        }
    }

    /**
     * method for disconnecting.
     *
     * @author socho
     */
    public void disconnect() throws IOException {
        if (this.connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    public StackPane getCenterPane() {
        return centerPane;
    }

    public StackPane getRightPane() {
        return rightPane;
    }

    public String getUserName() { return this.userName; }

    public void setUserName (String user) { this.userName = user; }

    public boolean getLoggedIn() { return this.loggedIn; }

    public void setLoggedIn(boolean log) { this.loggedIn = log; }

    public Client getConnection(){
        return this.connection;
    }

    public Server getServer(){
        return this.server;
    }
}
