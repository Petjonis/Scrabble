package controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ChoiceBox;
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
  private Timer gameTimer;
  private int secondsPassed = 0;
  private boolean timerOn = false;

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
  private JFXButton undoButton;
  @FXML
  private ProgressBar progressBar;

  /**
   * Initializes the board and the dictionary and deactivates all buttons.
   */
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

  /**
   * Handling for pressing the pass button.
   * Returns all tiles pending confirmation to the rack and sends a Pass Message to the server.
   */
  @FXML
  void pass() throws IOException {
    undoMove();
    secondsPassed = 0;
    startTimer();
    progressBar.setVisible(false);
    MainController.mainController
        .getConnection()
        .sendToServer(new PassMessage(MainController.mainController.getUser()));
  }

  /**
   * Calls the {@code undoMove()} method on Button event.
   * All tiles pending confirmation are recalled to the rack.
   */
  @FXML
  void undo() {
    undoMove();
  }

  /**
   * If the player made a valid move, the played words and the tile rack which has
   * to be refilled, gets send to the server.
   * Stops the timer and clears the tiles pending confirmation to prepare the next
   * move.
   */
  @FXML
  void play() throws IOException {
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
      secondsPassed = 0;
      startTimer();
      progressBar.setVisible(false);
      System.out.println();
    }
  }

  /**
   * Swaps all tiles on the rack with new tiles from the servers tile bag.
   */
  @FXML
  void swap() throws IOException {
    undoMove();
    Tile[] tileRack = new Tile[tr.getTileRack().size()];
    tr.getTileRack().toArray(tileRack);
    for(Tile t : tileRack){
      if(t.getValue() == 0){
        t.setLetter(' ');
      }
    }
    startTimer();
    progressBar.setVisible(false);
    MainController.mainController
        .getConnection()
        .sendToServer(new SwapTilesMessage(tileRack, MainController.mainController.getUser()));
  }


  /**
   * Adds a dragover event handler to the target StackPanes on the board.
   * Accept it only if it is  not dragged from the same node
   * and if it has a string data.
   * @param target of drag event
   */
  public void setOnDragOver(StackPane target) {
    target.setOnDragOver(
        (DragEvent event) -> {
          if (event.getGestureSource() != target) {
            event.acceptTransferModes(TransferMode.MOVE);
          }
          event.consume();
        });
  }

  /**
   * Adds a drag entered event handler to the target StackPanes on the board.
   * Shows to the user that it is an actual gesture target by highlighting
   * the target pane in green.
   * @param target of drag event
   */
  public void setOnDragEntered(StackPane target) {
    target.setOnDragEntered(
        (DragEvent event) -> {
          if (event.getGestureSource() != target) {
            target.setStyle("-fx-background-color: green");
          }

          event.consume();
        });
  }

  /**
   * Adds a drag exited event handler to the target StackPanes on the board.
   * If the mouse moved away, remove the graphical cues.
   * @param target of drag event
   */
  public void setOnDragExited(StackPane target) {
    target.setOnDragExited(
        (DragEvent event) -> {
          int row = GridPane.getRowIndex(target);
          int col = GridPane.getColumnIndex(target);
          target.setStyle(getStyle(row, col));
          event.consume();
        });
  }

  /**
   * Adds a drag dropped event handler to the target StackPanes on the board.
   * If there is a string data on drag board, read it and use it to add the dragged
   * tile to the board.
   * Let the source know whether the string was successfully transferred and used
   * @param target of drag event
   */
  public void setOnDragDropped(StackPane target) {
    target.setOnDragDropped(
        (DragEvent event) -> {
          Dragboard db = event.getDragboard();
          boolean success = false;
          if (db.hasString()) {
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
          /*  */
          event.setDropCompleted(success);

          event.consume();
        });
  }

  /**
   * Adds a drag detected event handler to the sources of the drag events.
   * If drag was detected, start drag-and-drop gesture by putting a string on the drag board.
   * @param tile source of drag event.
   */
  public void setOnDragDetected(TileController tile) {
    tile.setOnDragDetected(
        (MouseEvent event) -> {
          Dragboard db = tile.startDragAndDrop(TransferMode.MOVE);
          db.setDragView(tile.snapshot(new SnapshotParameters(), null));
          tile.setVisible(true);
          ClipboardContent content = new ClipboardContent();
          content.putString(tile.getLetter() + "," + tile.getValue());
          db.setContent(content);

          event.consume();
        });
  }

  /**
   * Adds a drag done event handler to the sources of the drag events.
   * If the drag-and-drop gesture ended and the data was successfully moved, clear it.
   * @param tile source of drag event.
   */
  public void setOnDragDone(TileController tile) {
    tile.setOnDragDone(
        (DragEvent event) -> {
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

  /**
   * Adds a choice box to a blank tile when clicked, so the user can choose a Letter from A-Z
   *
   * @param tile a blank tile
   */
  public void setOnMouseClicked (TileController tile){
    tile.setOnMouseClicked(event ->  {
      ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("A", "B", "C", "D",
              "E", "F", "G", "H", "I", "J", "K",
              "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
              "V", "W", "X", "Y", "Z")
      );
      String[] stringToSet = new String[]{
              "A", "B", "C", "D",
              "E", "F", "G", "H", "I", "J", "K",
              "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
              "V", "W", "X", "Y", "Z"
      };

      cb.getSelectionModel().selectedIndexProperty().addListener((observableValue, value, new_value) -> {
        char letter = stringToSet[new_value.intValue()].charAt(0);
        int v = 0;
        Tile newTile = new Tile(letter,v,null);
        int col = GridPane.getColumnIndex(tile);
        tr.remove(col);
        tr.add(newTile);
      });
      tile.getChildren().add(cb);
    });
  }

  /**
   * Starts the graphical progressbar to indicate when a turn is over.
   */
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

  /**
   * Timer will be started or stopped by checking "timerOn" boolean. timer counts until 120 seconds
   * and after that it will send a pass message, stop the timer and the run method
   * (with purge() method).
   *
   * @author socho
   */
  public void startTimer() {
    if (timerOn) {
      gameTimer.cancel();
      timerOn = false;
    } else {
      gameTimer = new Timer();
      TimerTask activationTask = new TimerTask() {
        @Override
        public void run() {
          secondsPassed++;
          if (secondsPassed == 120) {
            try {
              Platform.runLater(
                      () -> undoMove());
              MainController.mainController.getConnection()
                  .sendToServer(new PassMessage(MainController.mainController
                      .getUser()));
              gameTimer.cancel();
              secondsPassed = 0;
              gameTimer.purge();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      };
      gameTimer.scheduleAtFixedRate(activationTask, 1000, 1000);
      timerOn = true;
    }
  }

  /**
   * Adds a StackPane to the game board and sets the target event handlers
   *
   * @param row the row of the board grid
   * @param col the column of the board grid
   */
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

  /**
   * Adds the tiles which got confirmed als playable to the board.
   * @param tiles the played tiles to add to the game board
   */
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

  /**
   * Removes a StackPane from the board grid.
   * @param row the row of the board grid
   * @param col the column of the board grid
   */
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

  /**
   * Used by the addStackPaneToBoardGrid() method to get the right color for the different
   * special squares.
   * @param row the row of the board grid
   * @param col the column of the board grid
   * @return a String with a color code for -fx-background-color
   */
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
      default:
        break;
    }
    return style;
  }

  /**
   * Renders the tile rack and adds the source event handlers to each
   * tile.
   */
  private void renderTileRack() {
    tileRack.getChildren().clear();
    for (Tile t : tr.getTileRack()) {
      TileController tc = new TileController();
      tc.setString(t.toString());
      if(tc.getLetter().isBlank()){
        setOnMouseClicked(tc);
      }else{
        setOnDragDetected(tc);
        setOnDragDone(tc);
      }
      tileRack.add(tc, tr.getTileRack().indexOf(t), 0);
    }
    for (int i = tr.getTileRack().size(); i < 7; i++) {
      StackPane stackPane = new StackPane();
      stackPane.setStyle("-fx-background-color:#878787; -fx-border-color: #000000");
      tileRack.add(stackPane, i, 0);
    }
  }

  /**
   * Deactivates all Buttons
   */
  public void deactivate() {
    tileRack.setDisable(true);
    boardGrid.setDisable(true);
    playButton.setDisable(true);
    passButton.setDisable(true);
    swapButton.setDisable(true);
    undoButton.setDisable(true);
    progressBar.setVisible(false);
  }

  /**
   * Activates all Buttons.
   */
  public void activate() {
    tileRack.setDisable(false);
    boardGrid.setDisable(false);
    playButton.setDisable(false);
    passButton.setDisable(false);
    swapButton.setDisable(false);
    undoButton.setDisable(false);
    progressBar.setVisible(true);
    startProgressBar();
    startTimer();
  }

  /**
   * Recalls all tiles from the board to the rack.
   */
  private void undoMove() {
    for (Tile t : board.getTilesPendingConfirmation()) {
      removeStackPaneFromBoardGrid(t.getRow(), t.getCol());
      addStackPaneToBoardGrid(t.getRow(), t.getCol());
      tr.add(new Tile(t.getLetter(), t.getValue(), null));
    }
    board.clearTilesPending();
  }

  /**
   * Sets the board inactive or active and initializes the tile rack.
   * @param tileRack Array of Tiles generated by the host.
   * @param isActivePlayer true if its the players turn, false otherwise.
   */
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

  /**
   * Returns the state of the timer.
   * @return true if timer is active, false otherwise.
   */
  public boolean isTimerOn() {
    return timerOn;
  }

}
