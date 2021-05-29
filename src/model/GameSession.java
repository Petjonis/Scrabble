package model;

import network.Server;

import java.util.ArrayList;
import java.util.Set;

/** class represents the game session which is essential for the network game. */
public class GameSession {

  private static int gameLobbyId = 0;
  private ArrayList<Player> players;
  private Server server;
  private Player host;
  private TileBag tilebag;

  /**
   * constructor for gameSession.
   *
   * @param portNumber for the port.
   * @author socho
   */
  public GameSession(int portNumber) {
    this.server = new Server(portNumber);
    this.server.setGameSession(this);
    this.players = new ArrayList<>();
    gameLobbyId++;
    this.tilebag = new TileBag();
  }

  /** getter and setter methods for all attributes. */

  /**
   * getter method for "players" ArrayList<Player>.
   *
   * @author socho
   */
  public ArrayList<Player> getPlayers() {
    return players;
  }

  /**
   * getter method for a player by a unique id.
   *
   * @author fjaehrli
   */
  public Player getPlayerByID(int ID) {
    for (Player p : players) {
      if (p.getPlayerID() == ID) {
        return p;
      }
    }
    return null;
  }

  /**
   * setter method for "host" Player instance.
   *
   * @author socho
   */
  public void setHost(Player hostPlayer) {
    this.host = hostPlayer;
  }

  /**
   * getter method for "server" Server instance.
   *
   * @author socho
   */
  public Server getServer() {
    return this.server;
  }

  /**
   * getter method for "tilebag" TileBag instance.
   *
   * @author fjaehrli
   */
  public TileBag getTilebag() {
    return this.tilebag;
  }
}
