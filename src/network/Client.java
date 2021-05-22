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
import model.ComputerPlayer;
import model.Player;

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
    Player user;
    String text;
    boolean token;
    while (running) {
      try {
        Message m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case UPDATE_PLAYERLIST:
            UpdatePlayerListMessage uplMsg = (UpdatePlayerListMessage) m;
            System.out.println("Players on the list: " + uplMsg.getActivePlayers());
            System.out.println(uplMsg.getPlayer().getUserName());
            ArrayList<Player> liste = uplMsg.getActivePlayers();
            for (Player p : liste) {
              System.out.println(p.getUserName());
            }
            GameInfoController.gameInfoController.updatePlayerList(liste);
            break;
          case REMOVE_PLAYERLIST:
            RemovingPlayerListMessage rplMsg = (RemovingPlayerListMessage) m;
            GameInfoController.gameInfoController.removePlayerFromPlayerList(rplMsg.getPlayer());
            break;
          case SEND_CHAT_MESSAGE:
            SendChatMessage scMsg = (SendChatMessage) m;
            user = scMsg.getPlayer();
            text = scMsg.getText();
            token = scMsg.getHosting();
            GameInfoController.gameInfoController.updateChat(user, text, token);
            break;
          case DISCONNECT:
            DisconnectMessage dcMsg = (DisconnectMessage) m;
            user = dcMsg.getPlayer();
            GameInfoController.gameInfoController.updateChat(new ComputerPlayer("[Server]"),user.getUserName() + " left the game.", false);
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
        this.out.writeObject(new DisconnectMessage(MainController.mainController.getUser(),MainController.mainController.getUser().getPlayerID()));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}