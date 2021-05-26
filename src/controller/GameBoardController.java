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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.Pair;
import messages.PassMessage;
import messages.PlayMessage;
import messages.SwapTilesMessage;
import model.Board;
import model.SquareType;
import model.Tile;
import model.TileRack;
import settings.GlobalSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
  private static final Integer STARTTIME = 120;
  public static GameBoardController gameBoardController;
  private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME * 100);
  private Board board;
  private TileRack tr;

  @FXML private GridPane boardGrid;
  @FXML private GridPane tileRack;
  @FXML private JFXButton playButton;
  @FXML private JFXButton passButton;
  @FXML private JFXButton swapButton;
  @FXML private JFXButton undoButton;
  @FXML private ProgressBar progressBar;

  @FXML
  void pass(ActionEvent event) throws IOException {
    undoMove();
    MainController.mainController
        .getConnection()
        .sendToServer(new PassMessage(MainController.mainController.getUser()));
  }

  @FXML
  void undo(ActionEvent event) {
    undoMove();
  }

  @FXML
  void play(ActionEvent event) throws IOException {
    ArrayList<Pair<String, Integer>> playedWords = board.playedWords();
    if (playedWords != null) {
      Tile[] tileRack = new Tile[tr.getTileRack().size()];
      tr.getTileRack().toArray(tileRack);
      MainController.mainController
          .getConnection()
          .sendToServer(
              new PlayMessage(
                  MainController.mainController.getUser(),
                  playedWords,
                  board.getTilesPendingConfirmation(),
                  tileRack));
      board.clearTilesPending();
      for (Pair<String, Integer> p : playedWords) {
        System.out.println("Word: " + p.getKey() + ", Score: " + p.getValue());
      }
      System.out.println();
    }
  }

  @FXML
  void swap(ActionEvent event) throws IOException {
    undoMove();
    Tile[] tileRack = new Tile[tr.getTileRack().size()];
    tr.getTileRack().toArray(tileRack);
    MainController.mainController
        .getConnection()
        .sendToServer(new SwapTilesMessage(tileRack, MainController.mainController.getUser()));
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    gameBoardController = this;

    board = new Board();
    board.initializeDictionary();

    // Adding StackPane to every Cell in the GridPane and Adding the Target Events to each
    // StackPane.
    for (int row = 0; row < GlobalSettings.ROWS; row++) {
      for (int col = 0; col < GlobalSettings.COLUMNS; col++) {
        addStackPaneToBoardGrid(row, col);
      }
    }

    deactivate();
  }

  // target event handlers
  public void setOnDragOver(StackPane target) {
    target.setOnDragOver(
        (DragEvent event) -> {
          /* data is dragged over the target */
          // System.out.println("onDragOver");

          /* accept it only if it is  not dragged from the same node
           * and if it has a string data */
          if (event.getGestureSource() != target) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.MOVE);
          }

          event.consume();
        });
  }

  public void setOnDragEntered(StackPane target) {
    target.setOnDragEntered(
        (DragEvent event) -> {
          /* the drag-and-drop gesture entered the target */
          // System.out.println("onDragEntered");
          /* show to the user that it is an actual gesture target */
          if (event.getGestureSource() != target) {
            target.setStyle("-fx-background-color: green");
          }

          event.consume();
        });
  }

  public void setOnDragExited(StackPane target) {
    target.setOnDragExited(
        (DragEvent event) -> {
          /* mouse moved away, remove the graphical cues */
          int row = GridPane.getRowIndex(target);
          int col = GridPane.getColumnIndex(target);
          target.setStyle(getStyle(row, col));
          event.consume();
        });
  }

  public void setOnDragDropped(StackPane target) {
    target.setOnDragDropped(
        (DragEvent event) -> {
          /* data dropped */
          // System.out.println("onDragDropped");
          /* if there is a string data on dragboard, read it and use it */
          Dragboard db = event.getDragboard();
          boolean success = false;
          if (db.hasString()) {
            // target.setText(db.getString());
            TileController tmp = new TileController();
            tmp.setString(db.getString());
            int row = GridPane.getRowIndex(target);
            int col = GridPane.getColumnIndex(target);
            setOnDragDetected(tmp);
            setOnDragDone(tmp);
            boardGrid.getChildren().remove(target);
            boardGrid.add(tmp, col, row);
            board.addTilePending(
                tmp.getLetter().charAt(0), Integer.parseInt(tmp.getValue()), row, col);
            success = true;
          }
          /* let the source know whether the string was successfully
           * transferred and used */
          event.setDropCompleted(success);

          event.consume();
        });
  }

  // source events handlers
  public void setOnDragDetected(TileController tile) {
    tile.setOnDragDetected(
        (MouseEvent event) -> {
          /* drag was detected, start drag-and-drop gesture*/
          // System.out.println("onDragDetected");

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
    tile.setOnDragDone(
        (DragEvent event) -> {
          /* the drag-and-drop gesture ended */
          // System.out.println("onDragDone");
          /* if the data was successfully moved, clear it */
          if (event.getTransferMode() == TransferMode.MOVE) {
            if (tileRack.getChildren().contains(tile)) {
              int col = GridPane.getColumnIndex(tile);
              tr.remove(col);
            } else if (boardGrid.getChildren().contains(tile)) {
              int row = GridPane.getRowIndex(tile);
              int col = GridPane.getColumnIndex(tile);
              board.removeTilePending(row, col);
              boardGrid.getChildren().remove(tile);
              addStackPaneToBoardGrid(row, col);
            }
          }

          event.consume();
        });
  }

  public void startProgressBar() {
    progressBar
        .progressProperty()
        .bind(timeSeconds.divide(STARTTIME * 100.0).subtract(1).multiply(-1));
    timeSeconds.set((STARTTIME + 1) * 100);
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .add(new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timeSeconds, 0)));
    timeline.playFromStart();
  }

  private void addStackPaneToBoardGrid(int row, int col) {
    StackPane stackPane = new StackPane();
    stackPane.setStyle(getStyle(row, col));
    if (board.getSquares()[row][col].getType() == SquareType.START) {
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

  public void addFinalTilesToBoardGrid(ArrayList<Tile> tiles) {
    for (Tile t : tiles) {
      removeStackPaneFromBoardGrid(t.getRow(), t.getCol());
      TileController tmp = new TileController();
      tmp.setString(t.toString());
      tmp.setStyle("-fx-background-color:#efcdaa");
      boardGrid.add(tmp, t.getCol(), t.getRow());
      board.placeTile(t.getLetter(), t.getValue(), t.getRow(), t.getCol());
    }
  }

  private void removeStackPaneFromBoardGrid(int row, int col) {
    for (Node node : boardGrid.getChildren()) {
      if (node instanceof StackPane
          && GridPane.getRowIndex(node) == row
          && GridPane.getColumnIndex(node) == col) {
        StackPane s = new StackPane(node);
        boardGrid.getChildren().remove(s);
        break;
      }
    }
  }

  private String getStyle(int row, int col) {
    String style = "";
    switch (board.getSquares()[row][col].getType()) {
      case DOUBLE_LETTER:
        style = "-fx-background-color:#aedcec";
        break;
      case DOUBLE_WORD:
      case START:
        style = "-fx-background-color:#f4ceca";
        break;
      case TRIPLE_LETTER:
        style = "-fx-background-color:#035ee3";
        break;
      case TRIPLE_WORD:
        style = "-fx-background-color:#ee3940";
        break;
      case NO_BONUS:
        style = "-fx-background-color:#eeeed2";
        break;
    }
    return style;
  }

  private void renderTileRack() {
    tileRack.getChildren().clear();
    for (Tile t : tr.getTileRack()) {
      TileController tc = new TileController();
      tc.setString(t.toString());
      setOnDragDetected(tc);
      setOnDragDone(tc);
      tileRack.add(tc, tr.getTileRack().indexOf(t), 0);
    }
    for (int i = tr.getTileRack().size(); i < 7; i++) {
      StackPane stackPane = new StackPane();
      stackPane.setStyle("-fx-background-color:#878787; -fx-border-color: #000000");
      tileRack.add(stackPane, i, 0);
    }
  }

  public void deactivate() {
    tileRack.setDisable(true);
    boardGrid.setDisable(true);
    playButton.setDisable(true);
    passButton.setDisable(true);
    swapButton.setDisable(true);
    undoButton.setDisable(true);
  }

  public void activate() {
    tileRack.setDisable(false);
    boardGrid.setDisable(false);
    playButton.setDisable(false);
    passButton.setDisable(false);
    swapButton.setDisable(false);
    undoButton.setDisable(false);
  }

  private void undoMove() {
    for (Tile t : board.getTilesPendingConfirmation()) {
      removeStackPaneFromBoardGrid(t.getRow(), t.getCol());
      addStackPaneToBoardGrid(t.getRow(), t.getCol());
      tr.add(new Tile(t.getLetter(), t.getValue(), null));
    }
    board.clearTilesPending();
  }

  public void initializeGame(Tile[] tileRack, boolean isActivePlayer) {
    tr = new TileRack(tileRack);
    renderTileRack();
    tr.registerChangeListener(c -> renderTileRack());
    if (isActivePlayer) {
      activate();
    } else {
      deactivate();
    }
  }

  public GridPane getTileRack() {
    return tileRack;
  }

  public JFXButton getPlayButton() {
    return playButton;
  }

  public JFXButton getPassButton() {
    return passButton;
  }

  public JFXButton getSwapButton() {
    return swapButton;
  }

  public JFXButton getUndoButton() {
    return undoButton;
  }

  public ProgressBar getProgressBar() {
    return progressBar;
  }
}
