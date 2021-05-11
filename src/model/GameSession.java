package model;

import controller.GameInfoController;
import controller.MainController;
import java.util.ArrayList;
import java.util.Set;
import network.Server;

public class GameSession {

  private static int gameLobbyId = 0;
  private String gameMode;
  private GameState state;
  private ArrayList<String> players;
  private Server server;
  private Player host;
  private static ArrayList<GameSession> sessions = new ArrayList<GameSession>();
  private GameInfoController gameInfoController ;

  public GameSession(int portNumber, Player maker) {
    this.server = new Server(portNumber, maker);
    gameLobbyId++;
    this.host = maker;
    this.state = GameState.WAITING_LOBBY;
    this.sessions.add(this);
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

  public ArrayList<String> getPlayers() {
    this.players = new ArrayList<String>(this.server.getClientNames());
    return players;
  }

  public void setHost(Player hostName) { this.host = hostName; }
  public Player getHost() { return this.host; }

  public Server getServer(){ return this.server; }

  public ArrayList<GameSession> getSessions() { return this.sessions;}

  public GameInfoController getGameInfoController() { return this.gameInfoController; }
  public void setGameInfoController (GameInfoController giControl) { this.gameInfoController = giControl; }
}
