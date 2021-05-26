package messages;

import javafx.util.Pair;
import model.Player;
import model.Tile;

import java.util.ArrayList;

public class PlayMessage extends Message {

  private ArrayList<Pair<String, Integer>> playedWords;
  private ArrayList<Tile> tilesPlayed;
  private Tile[] tileRack;

  /**
   * constructor for message.
   *
   * @param player is player who presses play.
   * @param playedWords contains the accepted played words.
   * @param tilesPlayed tiles that represent the played word.
   */
  public PlayMessage(
      Player player,
      ArrayList<Pair<String, Integer>> playedWords,
      ArrayList<Tile> tilesPlayed,
      Tile[] tileRack) {
    super(MessageType.PLAY_MESSAGE, player);
    this.playedWords = playedWords;
    this.tilesPlayed = tilesPlayed;
    this.tileRack = tileRack;
  }

  public ArrayList<Pair<String, Integer>> getPlayedWords() {
    return playedWords;
  }

  public ArrayList<Tile> getTilesPlayed() {
    return tilesPlayed;
  }

  public Tile[] getTileRack() {
    return tileRack;
  }
}
