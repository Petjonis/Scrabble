package network;

/**
 * This class contains all functions which is connected with the client(s).
 *
 * @author socho
 * @version 1.0
 */

import com.sun.tools.javac.Main;
import controller.GameBoardController;
import controller.GameInfoController;
import controller.MainController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import messages.*;
import model.GameSession;
import model.Player;
import model.Square;
import model.Tile;

public class ServerProtocol extends Thread {

  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private Server server;
  private GameSession gameSession;
  private Player client;
  private boolean running = true;
  public static int id = 0;

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


  /**
   * sends to this client.
   */
  public void sendToClient(Message m) throws IOException {
    this.out.writeObject(m);
    out.flush();
    out.reset();
  }

  /**
   * close streams and socket.
   */
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
    Tile tile;
    Tile[] tiles;
    Square[][] position;
    Player user;
    int index;
    ArrayList<String> list;

    try {
      m = (Message) in.readObject();
      if (m.getMessageType() == MessageType.CONNECT) {
        ConnectMessage connectMsg = (ConnectMessage) m;
        user = connectMsg.getPlayer();
        index = connectMsg.getId();
        this.client = user;
        server.addClient(user, this);
        server.addIdToClient(index, user);

        this.gameSession.setPlayers(server.getClients());
        System.out.println(this.client.getUserName() + " was added to the Lobby.");
      } else {
        disconnect();
      }

      /** while the server runs many different messages will reach the server.*/
      while (running) {
        m = (Message) in.readObject();

        switch (m.getMessageType()) {
          case STARTGAME_FIRST:
            StartGameFirstMessage sgfMsg = (StartGameFirstMessage) m;
            break;
          case RESULT_MESSAGE:
            server.sendToAll(new ResultPlayerListMessage(server.getServerHost(),
                    new ArrayList<Player>(server.getClients())));
            break;
          case REQUEST_PLAYERLIST:
            server.sendToAll(new UpdatePlayerListMessage(server.getServerHost(),
                new ArrayList<Player>(server.getClients())));
            break;
          case LEAVE_GAME:
            LeaveGameMessage lgMsg = (LeaveGameMessage) m;
            user = lgMsg.getPlayer();
            if (server.getIds().contains(lgMsg.getId())) {
              server
                  .sendToAllBut(lgMsg.getId(), new RemovingPlayerListMessage(user, lgMsg.getId()));
              server.removeClient(user);
              server.removeIdToClient(lgMsg.getId(), user);
              server.getGameSession().setPlayers(server.getClients());
            }
            break;
          case DISCONNECT:
            DisconnectMessage dcMsg = (DisconnectMessage) m;
            user = dcMsg.getPlayer();
            server.sendToAllBut(user.getPlayerID(), new DisconnectMessage(user, dcMsg.getId()));
            server.removeClient(user);
            server.removeIdToClient(dcMsg.getId(), user);
            System.out.println(user.getUserName() + " left the Lobby.");
            break;
          case SERVERSHUTDOWN:
            ShutDownMessage sdMsg = (ShutDownMessage) m;
            server.sendToAllBut(sdMsg.getId(), new ShutDownMessage(sdMsg.getPlayer(),
                sdMsg.getId()));
            server.stopServer();
            MainController.mainController.setHosting(false);
            break;
          case PASS_MESSAGE:
            PassMessage passMsg = (PassMessage) m;
            user = passMsg.getPlayer();
            break;
          case SEND_TILE:
            SendTileMessage stMsg = (SendTileMessage) m;
            tile = stMsg.getTile();
            position = stMsg.getPosition();
            user = stMsg.getPlayer();
            server.sendToAll(stMsg);
            break;
          case SEND_CHAT_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            user = scMsg.getPlayer();
            server.sendToAllBut(user.getPlayerID(), scMsg);
            break;
          case SWAP_TILES:
            SwapTilesMessage swtMsg = (SwapTilesMessage) m;
            user = swtMsg.getPlayer();
            tiles = swtMsg.getTiles();
            /** put them into bag and then draw? or draw and put back after?
             drawable if rack already full? */
            gameSession.getTilebag().addTiles(tiles);
            break;
          default:
            break;
        }

      }

    } catch (IOException e) {
      running = false;
      if (socket.isClosed()) {
        System.out.println("Socket was closed. Client: " + client);
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
}