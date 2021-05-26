package network;

/**
 * This class establish the connection to the server and operates the client-side.
 *
 * @author socho
 * @version 1.0
 */

import controller.GameBoardController;
import controller.GameInfoController;
import controller.MainController;
import controller.ResultController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Platform;
import messages.ConnectMessage;
import messages.*;
import model.ComputerPlayer;
import model.Player;
import model.Tile;

public class Client extends Thread {

  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean running = true;

  /**
   * Constructor for the client.
   */
  public Client(String ip, int port) throws IOException {
    try {

      this.clientSocket = new Socket(ip, port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());

    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.out.println("Could not establish connection to " + ip + ":" + port);
    }
  }

  /**
   * checks if the connection is alright.
   */
  public boolean isOk() {
    return (clientSocket != null) && (clientSocket.isConnected()) && !(clientSocket.isClosed());
  }

  /**
   * this method is crucial for getting the info for the client-side.
   */
  public void run() {
    while (running) {
      try {
        Message m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case CONNECT:
            ConnectMessage connectMsg = (ConnectMessage) m;
            MainController.mainController.getUser().setPlayerID(connectMsg.getPlayer().getPlayerID());
            break;
          case STARTGAME_FIRST:
            StartGameFirstMessage sgfMsg = (StartGameFirstMessage) m;
            Tile [] playerTiles = sgfMsg.getTiles();
            boolean activePlayer = sgfMsg.getActive();
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                GameBoardController.gameBoardController.initializeGame(playerTiles, activePlayer);
                GameInfoController.gameInfoController.setActivePlayer(1);
              }
            });
            break;
          case START_PLAY:
            StartPlayMessage spMsg = (StartPlayMessage) m;
            Player nextPlayer = spMsg.getPlayer();
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                if(nextPlayer.getPlayerID() == MainController.mainController.getUser().getPlayerID()){
                  GameBoardController.gameBoardController.activate();
                }
                GameInfoController.gameInfoController.setActivePlayer(nextPlayer.getPlayerID());
              }
            });
            break;
          case END_PLAY:
            EndPlayMessage epMsg = (EndPlayMessage) m;
            Tile[] newTileRack = epMsg.getTiles();
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                GameBoardController.gameBoardController.initializeGame(newTileRack, false);
              }
            });
            break;
          case PLAY_MESSAGE:
            PlayMessage pMsg = (PlayMessage) m;
            Player nxtPlayer = pMsg.getPlayer();
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                GameBoardController.gameBoardController.addFinalTilesToBoardGrid(pMsg.getTiles());
                GameInfoController.gameInfoController.updateLastWordList(pMsg.getPlayedWords());
                GameInfoController.gameInfoController.updateScoreBoard(
                        pMsg.getPlayer().getPlayerID(),pMsg.getPlayedWords());
              }
            });
            break;
          case UPDATE_PLAYERLIST:
            UpdatePlayerListMessage uplMsg = (UpdatePlayerListMessage) m;
            ArrayList<Player> liste = uplMsg.getActivePlayers();
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                GameInfoController.gameInfoController.updatePlayerList(liste);
              }
            });
            break;
          case REMOVE_PLAYERLIST:
            RemovingPlayerListMessage rplMsg = (RemovingPlayerListMessage) m;
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                GameInfoController.gameInfoController
                    .removePlayerFromPlayerList(rplMsg.getPlayer().getUserName());
              }
            });
            break;
          case SEND_CHAT_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                GameInfoController.gameInfoController.updateChat(scMsg.getPlayer(), scMsg.getText(),
                    scMsg.getHosting());
              }
            });
            break;
          case PASS_MESSAGE:
            GameBoardController.gameBoardController.activate();
            break;
          case DISCONNECT:
            DisconnectMessage dcMsg = (DisconnectMessage) m;
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                GameInfoController.gameInfoController
                    .updateChat(new ComputerPlayer("[Server]"),
                        dcMsg.getPlayer().getUserName() + " left the game.",
                        false);
              }
            });
            break;
          case SERVERSHUTDOWN:
            MainController.mainController.disconnect();
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                try {
                  MainController.mainController
                      .changePane(MainController.mainController.getCenterPane(),
                          "/view/Start.fxml");
                } catch (IOException e) {
                  e.printStackTrace();
                }
                MainController.mainController.getRightPane().getChildren().clear();

                MainController.mainController.getPlayButton().setDisable(false);
              }
            });
            break;
          case END_GAME:
            EndGameMessage egMsg = (EndGameMessage) m;
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    try {
                      MainController.mainController.changePane(
                          MainController.mainController.getCenterPane(), "/view/Result.fxml");
                      ResultController.resultController.setPlayers(egMsg.getPlayers());
                      ResultController.resultController.setShizzl();
                      MainController.mainController.getRightPane().getChildren().clear();
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  }
                });
            break;
          default:
            break;
        }
      } catch (ClassNotFoundException | IOException e) {
        break;
      }
    }
  }

  /**
   * method for sending out a message to the server.
   */
  public void sendToServer(Message m) throws IOException {
    this.out.writeObject(m);
    out.flush();
    out.reset();
  }

  /**
   * clients disconnects and closes sockets.
   */
  public void disconnect() {
    running = false;
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new DisconnectMessage(MainController.mainController.getUser(),
            MainController.mainController.getUser().getPlayerID()));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}