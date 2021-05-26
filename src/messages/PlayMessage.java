package messages;

import javafx.util.Pair;
import model.Player;
import model.Tile;

import java.util.ArrayList;

public class PlayMessage extends Message {

	private ArrayList<Pair<String, Integer>> playedWords;
	private ArrayList<Tile> tiles;
	private Tile[] tileRack;

	/**
	 * constructor for message.
	 *
	 * @param player is player who presses play.
	 * @param playedWords contains the accepted played words.
	 * @param tiles are all the tiles who represent the words.
	 */
	public PlayMessage(Player player, ArrayList<Pair<String, Integer>> playedWords, ArrayList<Tile> tiles, Tile[] tileRack) {
		super(MessageType.PLAY_MESSAGE, player);
		this.playedWords = playedWords;
		this.tiles = tiles;
		this.tileRack = tileRack;
	}

	public ArrayList<Pair<String, Integer>> getPlayedWords() {
		return playedWords;
	}

	public ArrayList<Tile> getTiles(){ return tiles; }

	public Tile[] getTileRack(){ return tileRack; }
}

