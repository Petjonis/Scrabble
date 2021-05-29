package view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ConversationView extends VBox {
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();
    private ScrollPane messageScroller;
    private VBox messageContainer;

    public ConversationView(){
        super(5);
        setupElements();
    }

    private void setupElements(){
        setupMessageDisplay();
        getChildren().setAll(messageScroller);
        setPadding(new Insets(5));
    }

    private void setupMessageDisplay(){
        messageContainer = new VBox(5);
        messageContainer.setStyle("-fx-background-color: #312E2B");
        Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());

        messageScroller = new ScrollPane(messageContainer);
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //messageScroller.setPrefHeight(300);
        messageScroller.prefHeightProperty().bind(messageContainer.prefWidthProperty());
        messageScroller.prefWidthProperty().bind(messageContainer.prefWidthProperty());
        messageScroller.setFitToWidth(true);
        //Make the scroller scroll to the bottom when a new message is added
        messageScroller.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                messageScroller.setVvalue(1.0);
            }
        });
    }

    public void sendMessage(String name,String message){
        speechBubbles.add(new SpeechBox(name, message, SpeechDirection.RIGHT));
    }

    public void receiveMessage(String name,String message){
        speechBubbles.add(new SpeechBox(name, message, SpeechDirection.LEFT));
    }
}
