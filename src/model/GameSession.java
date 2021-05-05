package model;

import java.util.ArrayList;
import network.Server;

public class GameSession {

  private static int gameLobbyId = 0;
  private String gameMode;
  private GameState state;
  private ArrayList<String> players;
  private int port;
  private Server server;

  public GameSession() {
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

  public ArrayList getPlayers() {
    this.players = new ArrayList<String>(server.getClientNames());
    return players;
  }

  public void setPort(int portNumber){ this.port = portNumber; }
  public int getPort() { return this.port; }
}
