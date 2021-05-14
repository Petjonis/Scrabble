package network;

/**
 * This class establish the connection to the server and operates the client-side.
 *
 * @author socho
 * @version 1.0
 */

import controller.GameInfoController;
import controller.MainController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import messages.ApproveConnectMessage;
import messages.DisconnectMessage;
import messages.Message;
import messages.SendChatMessage;
import messages.SendInitialDataMessage;
import messages.UpdatePlayerListMessage;
import model.GameSession;

public class Client extends Thread {

  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean running = true;
  private GameSession gameSession;

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
          case APPROVE_CONNECT:
            ApproveConnectMessage acMsg = (ApproveConnectMessage) m;
            MainController.mainController.getUser().setActiveSession(acMsg.getGameSession());
            break;
          case UPDATE_PLAYLIST:
            UpdatePlayerListMessage uplMsg = (UpdatePlayerListMessage) m;
            GameInfoController.gameInfoController.updatePlayerList(uplMsg.getActivePlayers());
            break;
          case SEND_INITIAL_DATA:
            SendInitialDataMessage sendInitialDataMes = (SendInitialDataMessage) m;
            break;
          case SEND_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            String user = scMsg.getFrom();
            String text = scMsg.getText();
            GameInfoController.gameInfoController.updateChat(user, text);
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
        this.out.writeObject(new DisconnectMessage(new String("user")));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * getter and setter methods for attributes.
   */

  public void setGameSession(GameSession session) {
    this.gameSession = session;
  }

  public GameSession getGameSession() {
    return this.gameSession;
  }
}