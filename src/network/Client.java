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
import java.util.ArrayList;
import javafx.event.ActionEvent;
import messages.DisconnectMessage;
import messages.Message;
import messages.RemovingPlayerListMessage;
import messages.SendChatMessage;
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
    String user, text;
    while (running) {
      try {
        Message m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case UPDATE_PLAYERLIST:
            UpdatePlayerListMessage uplMsg = (UpdatePlayerListMessage) m;
            System.out.println("Players on the list: " + uplMsg.getActivePlayers());
            ArrayList<String> liste = uplMsg.getActivePlayers();
            GameInfoController.gameInfoController.updatePlayerList(liste);
            break;
          case REMOVE_PLAYERLIST:
            RemovingPlayerListMessage rplMsg = (RemovingPlayerListMessage) m;
            GameInfoController.gameInfoController.removePlayerFromPlayerList(rplMsg.getFrom());
            break;
          case SEND_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            user = scMsg.getFrom();
            text = scMsg.getText();
            boolean token = scMsg.getHosting();
            GameInfoController.gameInfoController.updateChat(user, text, token);
            break;
          case DISCONNECT:
            DisconnectMessage dcMsg = (DisconnectMessage) m;
            user = dcMsg.getFrom();
            GameInfoController.gameInfoController.updateChat("[System]",user + " left the game.", false);
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
        this.out.writeObject(new DisconnectMessage(MainController.mainController.getUser().getUserName()));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}