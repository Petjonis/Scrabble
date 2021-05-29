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
import model.Player;

public class Server {

  private ServerSocket hostSocket;
  private boolean running;
  private int port;
  private GameSession gameSession;
  private Player serverHost;
  /** collects all connected clients' user names in a HashMap. */
  private HashMap<Player, ServerProtocol> clients = new HashMap<>();

  /**
   * constructor for the server.
   *
   * @param portNumber is for the port, which client needs to connect to the server.
   * @author socho
   */
  public Server(int portNumber) {
    this.port = portNumber;
  }

  /**
   * method to remove a client from the clients HashMap.
   *
   * @author socho
   */
  public synchronized void removeClient(Player client) {
    this.clients.remove(client);
  }

  /**
   * method to check if a client is already in the clients HashMap.
   *
   * @author socho
   */
  public synchronized boolean userExistsP(Player client) {
    return this.clients.containsKey(client);
  }

  /**
   * method to add the client to the clients HashMap.
   *
   * @author socho
   */
  public synchronized void addClient(Player client, ServerProtocol protocol) {
    this.clients.put(client, protocol);
  }

  /**
   * method to get a new HashSet<Player> of the clients who are already in the clients HashMap.
   *
   * @author socho
   */
  public synchronized Set<Player> getClients() {
    Set<Player> clientNames = this.clients.keySet();
    return new HashSet<Player>(clientNames);
  }

  /**
   * setup server + listen to connection requests from clients (up to four clients).
   *
   * @author socho
   */
  public void listen() throws IOException {
    running = true;
    try {
      hostSocket = new ServerSocket(this.port);
      System.out.println("Server runs...");

      while (running) {
        if (clients.size() < 4) {
          Socket clientSocket = hostSocket.accept();

          ServerProtocol clientConnectionThread =
              new ServerProtocol(clientSocket, this, this.gameSession);
          clientConnectionThread.start();
        } else {
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
   * method for sending messages (server-side).
   *
   * @author socho
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
      removeClient(c);
    }
  }

  /**
   * send to all clients who are connected to the server.
   *
   * @author socho
   */
  public void sendToAll(Message m) {
    sendTo(new ArrayList<Player>(getClients()), (Message) (m.clone()));
  }

  /**
   * sending to a specific list of client/s.
   *
   * @author socho
   */
  public void sendToAll(ArrayList<Player> list, Message m) {
    sendTo(list, (Message) (m.clone()));
  }

  /**
   * stops the server and closes ServerSocket.
   *
   * @author socho
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
   * getter method for port.
   *
   * @author socho
   */
  public int getPort() {
    return this.port;
  }

  /**
   * setter method for port.
   *
   * @author socho
   */
  public void setPort(int portNumber) {
    this.port = portNumber;
  }

  /**
   * getter method for gameSession.
   *
   * @author socho
   */
  public GameSession getGameSession() {
    return this.gameSession;
  }

  /**
   * setter method for port.
   *
   * @author socho
   */
  public void setGameSession(GameSession session) {
    this.gameSession = session;
  }

  /**
   * getter method for serverHost.
   *
   * @author socho
   */
  public Player getServerHost() {
    return this.serverHost;
  }

  /**
   * setter method for serverHost.
   *
   * @author socho
   */
  public void setServerHost(Player user) {
    this.serverHost = user;
  }

  /**
   * getter method for the clients HashMap.
   *
   * @author socho
   */
  public HashMap<Player, ServerProtocol> getClientsHashMap() {
    return this.clients;
  }
}
