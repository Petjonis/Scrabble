/**
 * This class establish the connection to the server and operates the client-side.
 *
 * @author socho
 * @author fpetek
 * @version 1.0
 */
package network;

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
import messages.DisconnectMessage;
import messages.EndGameMessage;
import messages.EndPlayMessage;
import messages.Message;
import messages.PassMessage;
import messages.PlayMessage;
import messages.RemovingPlayerListMessage;
import messages.SendChatMessage;
import messages.StartGameFirstMessage;
import messages.StartPlayMessage;
import messages.UpdatePlayerListMessage;
import model.Player;
import model.Tile;

public class Client extends Thread {

  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean running = true;

  /** Constructor for the client. */
  public Client(String ip, int port) throws IOException {
    try {

      this.clientSocket = new Socket(ip, port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());

    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.out.println(
          MainController.mainController.getUser().getUserName()
              + " could not establish connection to "
              + ip
              + ":"
              + port);
    }
  }

  /** checks if the connection is alright. */
  public boolean isOk() {
    return (clientSocket != null) && (clientSocket.isConnected()) && !(clientSocket.isClosed());
  }

  /** this method is crucial for getting the info for the client-side. */
  public void run() {
    while (running) {
      try {
        Message m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case CONNECT:
            ConnectMessage connectMsg = (ConnectMessage) m;
            MainController.mainController
                .getUser()
                .setPlayerID(connectMsg.getPlayer().getPlayerID());
            break;
          case STARTGAME_FIRST:
            StartGameFirstMessage sgfMsg = (StartGameFirstMessage) m;
            Tile[] playerTiles = sgfMsg.getTiles();
            boolean activePlayer = sgfMsg.getActive();
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    GameBoardController.gameBoardController.initializeGame(
                        playerTiles, activePlayer);
                    GameInfoController.gameInfoController.setActivePlayerByIndex(1);
                  }
                });
            break;
          case START_PLAY:
            StartPlayMessage spMsg = (StartPlayMessage) m;
            Player nextPlayer = spMsg.getPlayer();
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    if (nextPlayer.getPlayerID()
                        == MainController.mainController.getUser().getPlayerID()) {
                      GameBoardController.gameBoardController.activate();
                    }
                    GameInfoController.gameInfoController.setActivePlayerByName(
                        nextPlayer.getUserName());
                  }
                });
            break;
          case END_PLAY:
            EndPlayMessage epMsg = (EndPlayMessage) m;
            Tile[] newTileRack = epMsg.getTiles();
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    GameBoardController.gameBoardController.initializeGame(newTileRack, false);
                  }
                });
            break;
          case PLAY_MESSAGE:
            PlayMessage pMsg = (PlayMessage) m;
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    GameBoardController.gameBoardController.addFinalTilesToBoardGrid(
                        pMsg.getTilesPlayed());
                    GameInfoController.gameInfoController.updateLastWordList(pMsg.getPlayedWords());
                    GameInfoController.gameInfoController.updateScoreBoard(
                        pMsg.getPlayer().getPlayerID(),
                        pMsg.getPlayedWords(),
                        pMsg.getTilesPlayed().size() == 7);
                  }
                });
            break;
          case UPDATE_PLAYERLIST:
            UpdatePlayerListMessage uplMsg = (UpdatePlayerListMessage) m;
            ArrayList<Player> liste = uplMsg.getActivePlayers();
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    GameInfoController.gameInfoController.updatePlayerList(liste);
                  }
                });
            break;
          case REMOVE_PLAYERLIST:
            RemovingPlayerListMessage rplMsg = (RemovingPlayerListMessage) m;
            String user = rplMsg.getName();
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    GameInfoController.gameInfoController.removePlayerFromPlayerList(user);
                  }
                });
            break;
          case SEND_CHAT_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    if (scMsg.getPlayer().getPlayerID()
                        == MainController.mainController.getUser().getPlayerID()) {
                      GameInfoController.gameInfoController.updateChatSent(
                          scMsg.getPlayer(), scMsg.getText(), scMsg.getHosting());
                    } else {
                      GameInfoController.gameInfoController.updateChatReceived(
                          scMsg.getPlayer(), scMsg.getText(), scMsg.getHosting());
                    }
                  }
                });
            break;
          case PASS_MESSAGE:
            PassMessage passMsg = (PassMessage) m;
            Player nextP = passMsg.getPlayer();
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    if (nextP.getPlayerID()
                        == MainController.mainController.getUser().getPlayerID()) {
                      GameBoardController.gameBoardController.activate();
                    } else {
                      GameBoardController.gameBoardController.deactivate();
                    }
                    GameInfoController.gameInfoController.setActivePlayerByName(
                        nextP.getUserName());
                    System.out.println(nextP.getUserName() + " ist dran");
                  }
                });
            break;
          case DISCONNECT:
            DisconnectMessage dcMsg = (DisconnectMessage) m;
            String user1 = dcMsg.getName();
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    GameInfoController.gameInfoController.addSystemMessage(
                        "[Server]: " + user1 + " left the game.");
                  }
                });
            break;
          case SERVERSHUTDOWN:
            MainController.mainController.disconnect();
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    try {
                      MainController.mainController.changePane(
                          MainController.mainController.getCenterPane(), "/view/Start.fxml");
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
                      ResultController.resultController.setResults();
                      ResultController.resultController.addResultsToDatabase();
                      MainController.mainController.reloadStats();
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

  /** method for sending out a message to the server. */
  public void sendToServer(Message m) throws IOException {
    this.out.writeObject(m);
    out.flush();
    out.reset();
  }

  /** clients disconnects and closes sockets. */
  public void disconnect() {
    running = false;
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(
            new DisconnectMessage(
                MainController.mainController.getUser(),
                MainController.mainController.getUser().getUserName()));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
