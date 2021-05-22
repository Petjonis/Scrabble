package network;

/**
 * This class contains all functions which is connected with the client(s).
 *
 * @author socho
 * @version 1.0
 */

import com.sun.tools.javac.Main;
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
  private Player clientName;
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
    Square [][] position;
    Player user;
    int index;
    ArrayList<String> list;


    try {
      m = (Message) in.readObject();
      if (m.getMessageType() == MessageType.CONNECT) {
        ConnectMessage cMsg = (ConnectMessage) m;
        user = cMsg.getFrom();
        index = cMsg.getId();
        this.clientName = user;
        if(server.getClientNames().size() == 0) {
          server.addClient(user, this);
          server.addIdToClient(index, user);
        }else{
          server.addClient(user, this);
          server.addIdToClient(index, user);
        }
        this.gameSession.setPlayerNames(server.getClientNames());
        System.out.println(this.clientName.getUserName() + " was added to the Lobby.");
        /** checking for who is in the same one lobby. */
        System.out.println(this.gameSession.getPlayerNames());
        System.out.println(server.getIds());
      } else {
        disconnect();
      }

      /** while the server runs many different messages will reach the server.*/
      while (running) {
        m = (Message) in.readObject();

        switch (m.getMessageType()) {
          case REQUEST_PLAYERLIST:
            server.sendToAll(new UpdatePlayerListMessage(server.getServerHost(), new ArrayList<Player>(server.getClientNames())));
            break;
          case LEAVE_GAME:
            LeaveGameMessage lgMsg = (LeaveGameMessage) m ;
            user = lgMsg.getFrom();
            if (server.userExistsP(user) && user.equals(server.getUserFromId(lgMsg.getId()))) {
              server.removeClient(user);
              server.removeIdToClient(lgMsg.getId(),user );
              server.getGameSession().setPlayerNames(server.getClientNames());
              server.sendToAll(new RemovingPlayerListMessage(lgMsg.getFrom()));
            }
            break;
          case DISCONNECT:
            DisconnectMessage dcMsg = (DisconnectMessage) m;
            user = dcMsg.getFrom();
            server.sendToAllBut(user, new DisconnectMessage(user, dcMsg.getId()));
            server.removeClient(user);
            server.removeIdToClient(dcMsg.getId(), user);
            System.out.println(user + " left the Lobby.");
            break;
          case PASS_MESSAGE:
            PassMessage pMsg = (PassMessage) m;
            user = pMsg.getFrom();
            break;
          case SEND_TILE:
            SendTileMessage stMsg = (SendTileMessage) m;
            tile = stMsg.getTile();
            position = stMsg.getPosition();
            user = stMsg.getFrom();
            server.sendToAll(stMsg);
            break;
          case SEND_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            user = scMsg.getFrom();
            if (scMsg.getHosting()) {
              server.sendToAllBut(server.getServerHost(), scMsg);
            }else{
              server.sendToAllBut(user, scMsg);
            }
            break;
          case SWAP_TILES:
            SwapTilesMessage swtMsg = (SwapTilesMessage) m;
            user = swtMsg.getFrom();
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
        System.out.println("Socket was closed. Client: " + clientName);
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