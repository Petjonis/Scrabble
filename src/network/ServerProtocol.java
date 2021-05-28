package network;

/**
 * This class contains all functions which is connected with the client(s).
 *
 * @author socho
 * @author fpetek
 * @version 1.0
 */

import controller.GameInfoController;
import controller.MainController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javafx.util.Pair;
import messages.ConnectMessage;
import messages.DisconnectMessage;
import messages.EndGameMessage;
import messages.EndPlayMessage;
import messages.LeaveGameMessage;
import messages.Message;
import messages.MessageType;
import messages.PassMessage;
import messages.PlayMessage;
import messages.RemovingPlayerListMessage;
import messages.SendChatMessage;
import messages.ShutDownMessage;
import messages.StartPlayMessage;
import messages.SwapTilesMessage;
import messages.UpdatePlayerListMessage;
import model.GameSession;
import model.Player;
import model.Tile;
import model.TileRack;

public class ServerProtocol extends Thread {

  private static int passCount = 0;
  public Player user;
  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private Server server;
  private GameSession gameSession;
  private boolean running = true;

  ServerProtocol(Socket client, Server server, GameSession session) throws IOException {
    this.socket = client;
    this.server = server;
    this.gameSession = session;
    try {
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** sends to this client. */
  public void sendToClient(Message m) throws IOException {
    this.out.writeObject(m);
    out.flush();
    out.reset();
  }

  /** close streams and socket. */
  public void disconnect() {
    running = false;
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Clients will be connected to the server only when they send the Connect-Message. and Clients
   * will get a ApproveConnectMessage with the game session which the server host is part of. Server
   * sends a "UpdatePlayerList" message to all clients to update the player list.
   */
  public void run() {
    Message m;
    String name;
    Tile[] tiles;
    Player nextPlayer;
    TileRack playerTiles;
    int currentPlayerIndex;

    ArrayList<Pair<String, Integer>> words;

    try {
      m = (Message) in.readObject();
      if (m.getMessageType() == MessageType.CONNECT) {
        ConnectMessage connectMsg = (ConnectMessage) m;
        user = connectMsg.getPlayer();
        user.setPlayerID(gameSession.getPlayers().size());
        server.addClient(user, this);
        gameSession.getPlayers().add(user);
        sendToClient(new ConnectMessage(user));
        System.out.println(user.getUserName() + " was added to the Lobby.");
      } else {
        disconnect();
      }

      /** while the server runs many different messages will reach the server. */
      while (running) {
        m = (Message) in.readObject();

        switch (m.getMessageType()) {
          case PLAY_MESSAGE:
            passCount = 0;
            PlayMessage pMsg = (PlayMessage) m;
            words = pMsg.getPlayedWords();
            // Adding played points to player score
            if (pMsg.getTilesPlayed().size() == 7) {
              gameSession.getPlayerByID(pMsg.getPlayer().getPlayerID()).addScore(50);
            }
            for (Pair<String, Integer> p : words) {
              gameSession.getPlayerByID(pMsg.getPlayer().getPlayerID()).addScore(p.getValue());
            }
            currentPlayerIndex =
                gameSession
                    .getPlayers()
                    .indexOf(gameSession.getPlayerByID(pMsg.getPlayer().getPlayerID()));
            nextPlayer =
                gameSession
                    .getPlayers()
                    .get(
                        (currentPlayerIndex + 1 >= gameSession.getPlayers().size())
                            ? 0
                            : currentPlayerIndex + 1);
            server.sendToAll(
                new PlayMessage(
                    pMsg.getPlayer(),
                    pMsg.getPlayedWords(),
                    pMsg.getTilesPlayed(),
                    pMsg.getTileRack()));
            playerTiles = new TileRack(pMsg.getTileRack());
            playerTiles.refillFromBag(MainController.mainController.getGameSession().getTilebag());
            Tile[] newTileRack = new Tile[playerTiles.getTileRack().size()];
            playerTiles.getTileRack().toArray(newTileRack);
            if(playerTiles.getTileRack().isEmpty()){
              server.sendToAll(new EndGameMessage(pMsg.getPlayer(), server.getGameSession().getPlayers()));
            } else{
              sendToClient(new EndPlayMessage(pMsg.getPlayer(), newTileRack));
              server.sendToAll(new StartPlayMessage(nextPlayer));
            }
            break;
          case REQUEST_PLAYERLIST:
            server.sendToAll(
                new UpdatePlayerListMessage(
                    server.getServerHost(), server.getGameSession().getPlayers()));
            break;
          case LEAVE_GAME:
            LeaveGameMessage lgMsg = (LeaveGameMessage) m;
            user = lgMsg.getPlayer();
            name = lgMsg.getName();
            server.sendToAll(new RemovingPlayerListMessage(user, name));
            break;
          case DISCONNECT:
            DisconnectMessage dcMsg = (DisconnectMessage) m;
            user = dcMsg.getPlayer();
            name = dcMsg.getName();
            server.sendToAll(new DisconnectMessage(user, name));
            server.sendToAll(
                    new EndGameMessage(server.getServerHost(), server.getGameSession().getPlayers()));
            server.removeClient(user);
            gameSession.getPlayers().remove(user);
            System.out.println(user.getUserName() + " left the Lobby.");
            break;
          case SERVERSHUTDOWN:
            ShutDownMessage sdMsg = (ShutDownMessage) m;
            server.sendToAll(new ShutDownMessage(sdMsg.getPlayer(), sdMsg.getId()));
            server.stopServer();
            MainController.mainController.setHosting(false);
            break;
          case PASS_MESSAGE:
            PassMessage passMessage = (PassMessage) m;
            currentPlayerIndex =
                gameSession
                    .getPlayers()
                    .indexOf(gameSession.getPlayerByID(passMessage.getPlayer().getPlayerID()));
            nextPlayer =
                gameSession
                    .getPlayers()
                    .get(
                        (currentPlayerIndex + 1 >= gameSession.getPlayers().size())
                            ? 0
                            : currentPlayerIndex + 1);
            incrementPass();
            if (passCount == 6) {
              GameInfoController.gameInfoController.addSystemMessage("No Words played for six turns. GAME OVER!");
              server.sendToAll(
                  new EndGameMessage(server.getServerHost(), server.getGameSession().getPlayers()));
            }
            server.sendToAll(new PassMessage(nextPlayer));
            break;
          case SEND_CHAT_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            server.sendToAll(scMsg);
            break;
          case SWAP_TILES:
            incrementPass();
            if (passCount == 6) {
              GameInfoController.gameInfoController.addSystemMessage("No Words played for six turns. GAME OVER!");
              server.sendToAll(
                  new EndGameMessage(server.getServerHost(), server.getGameSession().getPlayers()));
            }
            SwapTilesMessage swtMsg = (SwapTilesMessage) m;
            currentPlayerIndex =
                gameSession
                    .getPlayers()
                    .indexOf(gameSession.getPlayerByID(swtMsg.getPlayer().getPlayerID()));
            nextPlayer =
                gameSession
                    .getPlayers()
                    .get(
                        (currentPlayerIndex + 1 >= gameSession.getPlayers().size())
                            ? 0
                            : currentPlayerIndex + 1);
            playerTiles = new TileRack(MainController.mainController.getGameSession().getTilebag());
            Tile[] newTilesRack = new Tile[playerTiles.getTileRack().size()];
            playerTiles.getTileRack().toArray(newTilesRack);
            MainController.mainController.getGameSession().getTilebag().addTiles(swtMsg.getTiles());
            sendToClient(new EndPlayMessage(swtMsg.getPlayer(), newTilesRack));
            server.sendToAll(new StartPlayMessage(nextPlayer));
            break;
          case END_GAME:
            server.sendToAll(
                new EndGameMessage(server.getServerHost(), server.getGameSession().getPlayers()));
          default:
            break;
        }
      }

    } catch (IOException e) {
      running = false;
      if (socket.isClosed()) {
        System.out.println("Socket was closed. Client: " + user);
      } else {
        try {
          socket.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    } catch (ClassNotFoundException e2) {
      System.out.println(e2.getMessage());
      e2.printStackTrace();
    }
  }

  public void incrementPass() {
    this.passCount++;
  }
}
