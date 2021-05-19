package controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.*;
import settings.GlobalSettings;

public class GameBoardController {
    private Board board;
    private TileBag tb;
    private TileRack tr;
    private static final Integer STARTTIME = 120;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME * 100);

    @FXML
    private VBox gameBoard;
    @FXML
    private GridPane boardGrid;
    @FXML
    private GridPane tileRack;
    @FXML
    private JFXButton playButton;
    @FXML
    private JFXButton passButton;
    @FXML
    private JFXButton swapButton;
    @FXML
    private ProgressBar progressBar;

    @FXML
    void pass(ActionEvent event) {
        /* TODO */
    }

    @FXML
    void play(ActionEvent event) {
        /* TODO */
    }
    @FXML
    void swap(ActionEvent event){
        /* TODO */
    }

    public void initialize() {
        board = new Board();
        //for test, should get tilebag from player
        tb = new TileBag();
        //for test, should get tilerack from player
        tr = new TileRack(tb);
        renderTileRack();
        tr.registerChangeListener(c -> renderTileRack());

        //Adding StackPane to every Cell in the GridPane and Adding the Target Events to each StackPane.
        for (int row = 0; row < GlobalSettings.ROWS; row++) {
            for (int col = 0; col < GlobalSettings.COLUMNS; col++) {
                addStackPaneToBoardGrid(row,col);
            }
        }

        //start demo progress bar
        //startProgressBar();

    }

    //target event handlers
    public void setOnDragOver(StackPane target) {
        target.setOnDragOver((DragEvent event) -> {
            /* data is dragged over the target */
            //System.out.println("onDragOver");

            /* accept it only if it is  not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != target
            ) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });
    }

    public void setOnDragEntered(StackPane target) {
        target.setOnDragEntered((DragEvent event) -> {
            /* the drag-and-drop gesture entered the target */
            //System.out.println("onDragEntered");
            /* show to the user that it is an actual gesture target */
            if (event.getGestureSource() != target
            ) {
                target.setStyle("-fx-background-color: green");
            }

            event.consume();
        });
    }

    public void setOnDragExited(StackPane target) {
        target.setOnDragExited((DragEvent event) -> {
            /* mouse moved away, remove the graphical cues */
            int row = GridPane.getRowIndex(target);
            int col = GridPane.getColumnIndex(target);
            target.setStyle("-fx-background-color: " + board.getSquares()[row][col].getColor() + ";");

            event.consume();
        });
    }

    public void setOnDragDropped(StackPane target) {
        target.setOnDragDropped((DragEvent event) -> {
            /* data dropped */
            //System.out.println("onDragDropped");
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                //target.setText(db.getString());
                TileController tmp = new TileController();
                tmp.setString(db.getString());
                setOnDragDetected(tmp);
                setOnDragDone(tmp);
                int row = GridPane.getRowIndex(target);
                int col = GridPane.getColumnIndex(target);
                boardGrid.getChildren().remove(target);
                boardGrid.add(tmp, col, row);
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
    }

    //source events handlers
    public void setOnDragDetected(TileController tile) {
        tile.setOnDragDetected(
                (MouseEvent event) -> {
                    /* drag was detected, start drag-and-drop gesture*/
                    //System.out.println("onDragDetected");

                    /* allow transfer mode move*/
                    Dragboard db = tile.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(tile.snapshot(new SnapshotParameters(), null));
                    tile.setVisible(true);
                    /* put a string on dragboard */
                    ClipboardContent content = new ClipboardContent();
                    content.putString(tile.getLetter() + "," + tile.getValue());
                    db.setContent(content);

                    event.consume();
                });
    }

    public void setOnDragDone(TileController tile) {
        tile.setOnDragDone((DragEvent event) -> {
            /* the drag-and-drop gesture ended */
            //System.out.println("onDragDone");
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                if (tileRack.getChildren().contains(tile)) {
                    int col = GridPane.getColumnIndex(tile);
                    tr.remove(col);
                    /*TEST
                    tr.refillFromBag();*/
                } else if (boardGrid.getChildren().contains(tile)) {
                    int row = GridPane.getRowIndex(tile);
                    int col = GridPane.getColumnIndex(tile);
                    boardGrid.getChildren().remove(tile);
                    addStackPaneToBoardGrid(row,col);
                }
            }

            event.consume();
        });
    }

    public void startProgressBar() {
        progressBar.progressProperty().bind(timeSeconds.divide(STARTTIME * 100.0).subtract(1).multiply(-1));
        timeSeconds.set((STARTTIME + 1) * 100);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
    }

    private void addStackPaneToBoardGrid(int row,int col){
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: " + board.getSquares()[row][col].getColor() + ";");
        if(board.getSquares()[row][col].getType() == SquareType.START){
            MaterialIconView m = new MaterialIconView(MaterialIcon.STAR);
            m.setSize("25");
            stackPane.getChildren().add(m);
        }
        setOnDragOver(stackPane);
        setOnDragEntered(stackPane);
        setOnDragExited(stackPane);
        setOnDragDropped(stackPane);
        boardGrid.add(stackPane, col, row);
    }

    private void renderTileRack(){
        tileRack.getChildren().clear();
        for(Tile t:tr.getTileRack()){
            TileController tc = new TileController();
            tc.setString(t.toString());
            setOnDragDetected(tc);
            setOnDragDone(tc);
            tileRack.add(tc, tr.getTileRack().indexOf(t), 0);
        }
    }
}
