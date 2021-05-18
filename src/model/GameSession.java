package model;

import java.util.ArrayList;
import java.util.Set;
import network.Server;

/** class represents the game session which is essential for the network game. */
public class GameSession {

  private static int gameLobbyId = 0;
  private static ArrayList<GameSession> sessions = new ArrayList<GameSession>();
  private GameState state;
  private ArrayList<String> players;
  private Server server;
  private Player host;
  private TileBag tilebag;

  public GameSession(int portNumber, Player host) {
    this.server = new Server(portNumber, host);
    this.server.setGameSession(this);
    gameLobbyId++;
    this.host = host;
    this.state = GameState.WAITING_LOBBY;
    this.tilebag = new TileBag();
    this.sessions.add(this);
  }

  public GameState getState() {
    return this.state;
  }

  /** getter and setter methods for all attributes. */
  public void setState(GameState gs) {
    this.state = gs;
  }

  public int getGameLobbyId() {
    return this.gameLobbyId;
  }

  public ArrayList<String> getPlayers() {
    this.players = new ArrayList<String>(this.server.getClientNames());
    return players;
  }

  public void setPlayers(Set<String> list) {
    this.players = new ArrayList<String>(list);
  }

  public Player getHost() {
    return this.host;
  }

  public void setHost(Player hostName) {
    this.host = hostName;
  }

  public Server getServer() {
    return this.server;
  }

  public ArrayList<GameSession> getSessions() {
    return this.sessions;
  }

  public TileBag getTilebag() {
    return this.tilebag;
  }
}
