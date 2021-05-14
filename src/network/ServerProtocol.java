package network;

/**
 * This class contains all functions which is connected with the client(s).
 *
 * @author socho
 * @version 1.0
 */

import com.sun.tools.javac.Main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import messages.ApproveConnectMessage;
import messages.Message;
import messages.MessageType;
import messages.SendChatMessage;
import messages.SendInitialDataMessage;
import messages.SendTileMessage;
import messages.UpdatePlayerListMessage;
import model.GameSession;
import model.Square;
import model.Tile;

public class ServerProtocol extends Thread {

  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private Server server;
  private GameSession gameSession;
  private String clientName;
  private boolean running = true;

  ServerProtocol(Socket client, Server server, GameSession session) throws IOException {
    this.socket = client;
    this.server = server;
    this.gameSession = session;
    try {
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
      sendInitialData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendInitialData() throws IOException {
    SendInitialDataMessage sendingDataMessage = new SendInitialDataMessage("host",
        server.getServerHost().getActiveSession().getPlayers(), new ArrayList<String>());
    sendToClient(sendingDataMessage);
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
   * will get a ApproveConnectMessage with the game session which the server host is part of.
   * Server sends a "UpdatePlayerList" message to all clients to update the player list.
   */
  public void run() {
    Message m;
    try {
      m = (Message) in.readObject();
      if (m.getMessageType() == MessageType.CONNECT) {
        String from = m.getFrom();
        this.clientName = from;
        server.sendToAllBut(server.getServerHost().getUserName(),
            new ApproveConnectMessage("host", server.getGameSession()));
        server.addClient(from, this);
        this.gameSession.setPlayers(server.getClientNames());
        server.sendToAll(
            new UpdatePlayerListMessage("host", new ArrayList<String>(server.getClientNames())));
        System.out.println(this.clientName + " was added to the Lobby.");
        /** checking for who is in the same one lobby. */
        System.out.println(this.gameSession.getPlayers());
      } else {
        disconnect();
      }

      /** while the server runs many different messages will reach the server.*/
      while (running) {
        m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case SEND_TILE:
            SendTileMessage stMsg = (SendTileMessage) m;
            Tile tile = stMsg.getTile();
            Square[][] position = stMsg.getPosition();
            String from = stMsg.getFrom();
            server.sendToAll(stMsg);
            break;
          case SEND_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            String user = scMsg.getFrom();
            server.sendToAllBut(user, scMsg);
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