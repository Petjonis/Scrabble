package model;

/**
 * Class represents a bag of tiles which has a maximum of 100 pieces.
 *
 * @author fpetek
 * @version 1.0
 */
public class TileBag {
  static final int MAX = 100;
  private Tile[] tiles = new Tile[MAX];

  /**
   * Constructor uses createClassicTiles method from Tile.java to initialize a classic set of tiles.
   */
  public TileBag() {
    this.tiles = Tile.createClassicTiles();
  }

  /**
   * Constructor to initialize a set of tiles with own value and frequency distribution.
   *
   * @param tiles Own tileset to initialize
   */
  public TileBag(Tile[] tiles) {
    this.tiles = tiles;
  }
}
