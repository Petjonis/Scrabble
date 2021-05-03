package network;

/**
 * This class establish the connection to the server and operates the client-side.
 *
 * @author socho
 * @version 1.0
 */

import controller.LoginController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import messages.ConnectMessage;
import messages.DisconnectMessage;
import messages.Message;

public class Client extends Thread {

  private String userName;
  private boolean host = false;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean loggedIn = false;
  private boolean running = true;

  /**
   * Constructor for the client.
   */
  public Client(String ip, int port) throws IOException {
    try {

      this.clientSocket = new Socket(ip, port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());

      /** delete comments-signs "//" when login works // when user has to log in to play.*/
     // LoginController lc1 = new LoginController();
     // this.userName = lc1.getUserName();
     // this.out.writeObject(new ConnectMessage(userName));

      this.out.writeObject(new ConnectMessage(new String("user")));
      out.flush();

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
    /**while(running){
     try{
     Message m = (Message) in.readObject();
     }catch(ClassNotFoundException | IOException e){
     break;
     }
     }*/
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
   * getter and setter method for the boolean "loggedIn".
   */
  public void setLoggedIn(boolean logged) {
    this.loggedIn = logged;
  }

  public boolean getLoggedIn() {
    return this.loggedIn;
  }

  /**
   * getter and setter method for the boolean "host".
   */
  public void setHost(boolean hosting) {
    this.host = hosting;
  }

  public boolean getHost() {
    return this.host;
  }

  public String getUserName(){
    return this.userName;
  }

}