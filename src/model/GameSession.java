package model;

import controller.MainController;
import java.util.ArrayList;
import java.util.Set;
import network.Server;

public class GameSession {

  private static int gameLobbyId = 0;
  private String gameMode;
  private GameState state;
  private ArrayList<String> players;
  private int port;
  private Server server;
  private String host;

  public GameSession(int portNumber) {
    this.server = new Server();
    this.port = portNumber;
    gameLobbyId++;
    this.players = getPlayers();
    this.state = GameState.WAITING_LOBBY;
  }

  public void setState(GameState gs) {
    this.state = gs;
  }

  public GameState getState() {
    return this.state;
  }

  public void setGameMode(String s) {
    this.gameMode = s;
  }

  public String getGameMode() {
    return this.gameMode;
  }

  public int getGameLobbyId() { return this.gameLobbyId; }

  public void setPlayers(Set<String> list){ this.players = new ArrayList<String>(list);}

  public ArrayList getPlayers() {
    this.players = new ArrayList<String>(this.server.getClientNames());
    return players;
  }

  public void setPort(int portNumber) { this.port = portNumber; }
  public int getPort() { return this.port; }

  public void setHost(String name) { this.host = name; }
  public String getHost() { return this.host; }

  public Server getServer(){ return this.server; }
}
