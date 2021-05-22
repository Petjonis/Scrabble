package network;

/**
 * This class is for setting up the server.
 *
 * @author socho
 * @version 1.0
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import messages.Message;
import model.GameSession;
import model.HumanPlayer;
import model.Player;

public class Server {

  private ServerSocket hostSocket;
  private boolean running;
  private int port;
  private GameSession gameSession;
  private Player serverHost;

  public Server(int portNumber) {
    this.port = portNumber;
  }

  /**
   * collects all connected clients' user names in a HashMap.
   */
  private HashMap<Player, ServerProtocol> clients = new HashMap<>();

  public synchronized void removeClient(Player clientName) {
    this.clients.remove(clientName);
  }

  public synchronized boolean userExistsP(Player name) {
    return this.clients.containsKey(name);
  }

  public synchronized void addClient(Player name, ServerProtocol protocol) {
    this.clients.put(name, protocol);
  }

  public synchronized Set<Player> getClientNames() {
    Set<Player> clientNames = this.clients.keySet();
    return new HashSet<Player>(clientNames);
  }

  /** every client user has a unique id which is also held in a HashMap.*/

  private HashMap<Integer, Player> clientsIDMap = new HashMap<>();

  public synchronized void addIdToClient(Integer id, Player clientName){
    clientsIDMap.put(id, clientName);
  }

  public synchronized Player getUserFromId(Integer id){
    return clientsIDMap.get(id);
  }

  public synchronized void removeIdToClient(Integer id, Player clientName){
    clientsIDMap.remove(id, clientName);
  }

  public synchronized Set<Integer> getIds() {
    Set<Integer> idNumbers = this.clientsIDMap.keySet();
    return new HashSet<Integer>(idNumbers);
  }

  /**
   * setup server + listen to connection requests from clients.
   */
  public void listen() throws IOException {
    running = true;
    try {
      hostSocket = new ServerSocket(this.port);
      System.out.println("Server runs");

      while (running) {
        if (clients.size() < 4) {
          Socket clientSocket = hostSocket.accept();

          ServerProtocol clientConnectionThread = new ServerProtocol(clientSocket, this,
              this.gameSession);
          clientConnectionThread.start();
        }else {
          System.out.println("This game session is full.");
        }
      }
    } catch (IOException e) {
      if (hostSocket != null && hostSocket.isClosed()) {
        System.out.println("Server stopped.");
      } else {
        e.printStackTrace();
      }
    }
  }

  /**
   * method for sending messages.
   */
  private synchronized void sendTo(List<Player> clientNames, Message m) {
    List<Player> clientFails = new ArrayList<Player>();
    for (Player clName : clientNames) {
      try {
        ServerProtocol c = clients.get(clName);
        c.sendToClient((Message) (m.clone()));
      } catch (IOException e) {
        clientFails.add(clName);
        continue;
      }
    }
    for (Player c : clientFails) {
      System.out.println("Client " + c + " removed (because of send failure).");
      removeClient(c);
    }
  }

  /**
   * send to all clients.
   */
  public void sendToAll(Message m) {
    sendTo(new ArrayList<Player>(getClientNames()), (Message) (m.clone()));
  }

  /**
   * sending to specific client/s.
   */
  public void sendToAll (ArrayList<Player> list, Message m){
    sendTo (list, (Message) (m.clone()));
  }

  /**
   * send to all clients except for one.
   */
  public void sendToAllBut(Player name, Message m) {
    synchronized (this.clients) {
      Set<Player> senderList = getClientNames();
      senderList.remove(name);
      sendTo(new ArrayList<Player>(senderList), m);
    }
  }

  /**
   * stops the server.
   */
  public void stopServer() {
    running = false;
    if (!hostSocket.isClosed()) {
      try {
        hostSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(0);
      }
    }
  }

  /**
   * getter and setter methods for attributes.
   */
  public int getPort() {
    return this.port;
  }

  public void setPort(int portNumber) {
    this.port = portNumber;
  }

  public GameSession getGameSession() {
    return this.gameSession;
  }

  public void setGameSession(GameSession session) {
    this.gameSession = session;
  }

  public void setServerHost(Player user) {
    this.serverHost = user;
  }

  public Player getServerHost() {
    return this.serverHost;
  }
}