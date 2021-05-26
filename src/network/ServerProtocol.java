package network;

/**
 * This class contains all functions which is connected with the client(s).
 *
 * @author socho
 * @version 1.0
 */

import controller.MainController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


import javafx.util.Pair;
import messages.*;
import model.*;

public class ServerProtocol extends Thread {

  public int id = 0;
  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private Server server;
  private GameSession gameSession;
  private boolean running = true;
  private static int passCount = 0 ;
  public Player user;

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
    int index;
    ArrayList<String> list;
    ArrayList<Pair<String, Integer>> words;

    try {
      m = (Message) in.readObject();
      if (m.getMessageType() == MessageType.CONNECT) {
        ConnectMessage connectMsg = (ConnectMessage) m;
        user = connectMsg.getPlayer();
        user.setPlayerID(gameSession.getPlayers().size());
        server.addClient(user, this);
        gameSession.getPlayers().add(user);
        server.addIdToClient(id, user);
        sendToClient(new ConnectMessage(user));
        System.out.println(user.getUserName() + " was added to the Lobby.");
      } else {
        disconnect();
      }

      /** while the server runs many different messages will reach the server.*/
      while (running) {
        m = (Message) in.readObject();

        switch (m.getMessageType()) {
          case PLAY_MESSAGE:
            passCount = 0;
            PlayMessage pMsg = (PlayMessage) m;
            words = pMsg.getPlayedWords();
            //Adding played points to player score
            if(pMsg.getTilesPlayed().size() == 7){
              gameSession.getPlayerByID(pMsg.getPlayer().getPlayerID()).addScore(50);
            }
            for(Pair<String, Integer> p : words){
              gameSession.getPlayerByID(pMsg.getPlayer().getPlayerID()).addScore(p.getValue());
            }
            int currentPlayerIndex = pMsg.getPlayer().getPlayerID();
            Player nextPlayer =  gameSession.getPlayers().get((currentPlayerIndex+1 >= gameSession.getPlayers().size()) ? 0 : currentPlayerIndex+1);
            server.sendToAll(new PlayMessage(pMsg.getPlayer(), pMsg.getPlayedWords(), pMsg.getTilesPlayed(),
                              pMsg.getTileRack()));
            TileRack playerTiles = new TileRack(pMsg.getTileRack());
            playerTiles.refillFromBag(MainController.mainController.getGameSession().getTilebag());
            Tile[] newTileRack = new Tile[7];
            playerTiles.getTileRack().toArray(newTileRack);
            sendToClient(new EndPlayMessage(pMsg.getPlayer(), newTileRack));
            server.sendToAll(new StartPlayMessage(nextPlayer));
            break;
          case REQUEST_PLAYERLIST:
            server.sendToAll(new UpdatePlayerListMessage(server.getServerHost(),
                server.getGameSession().getPlayers()));
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
            PassMessage passMessage = (PassMessage) m;
            int currentP = passMessage.getPlayer().getPlayerID();
            Player nextP =  gameSession.getPlayers().get((currentP+1 >= gameSession.getPlayers().size()) ? 0 : currentP+1);
            incrementPass();
            if (passCount == 6) {
              System.out.println("pass count is six. GAME OVER! ");
              server.sendToAll(new EndGameMessage(server.getServerHost(), server.getGameSession().getPlayers()));
            }
            server.sendToAll(new PassMessage(nextP));
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
            server.sendToAll(scMsg);
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

  public void incrementPass(){
    this.passCount++;
  }
}


