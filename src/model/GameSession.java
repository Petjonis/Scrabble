package model;

import network.Server;

import java.util.ArrayList;
import java.util.Set;

/** class represents the game session which is essential for the network game. */
public class GameSession {

  private static int gameLobbyId = 0;
  private static ArrayList<GameSession> sessions = new ArrayList<GameSession>();
  private GameState state;
  private ArrayList<Player> players;
  private Server server;
  private Player host;
  private TileBag tilebag;

  public GameSession(int portNumber) {
    this.server = new Server(portNumber);
    this.server.setGameSession(this);
    this.players = new ArrayList<>();
    gameLobbyId++;
    this.state = GameState.WAITING_LOBBY;
    this.tilebag = new TileBag();
    this.sessions.add(this);
  }

  /** getter and setter methods for all attributes. */
  public GameState getState() {
    return this.state;
  }

  public void setState(GameState gs) {
    this.state = gs;
  }

  public int getGameLobbyId() {
    return this.gameLobbyId;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public void setPlayers(Set<Player> list) {
    this.players = new ArrayList<Player>(list);
  }

  public Player getPlayerByID(int ID) {
    for (Player p : players) {
      if (p.getPlayerID() == ID) {
        return p;
      }
    }
    return null;
  }

  public Player getHost() {
    return this.host;
  }

  public void setHost(Player hostPlayer) {
    this.host = hostPlayer;
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
