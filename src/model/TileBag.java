/**
 * Class represents a bag of tiles which has a maximum of 100 pieces.
 *
 * @author fpetek
 * @version 1.0
 */
package model;

import settings.GlobalSettings;

public class TileBag {
  private Tile[] tiles = new Tile[GlobalSettings.MAXTILES];

  /**
   * Constructor uses createClassicTiles method from Tile.java to initialize a classic set of tiles.
   **/
  public TileBag() {
    this.tiles = Tile.createClassicTiles();
  }

  /**
   * Constructor to initialize a set of tiles with own value and frequency distribution.
   *
   * @param tiles Own tileset to initialize.
   **/
  public TileBag(Tile[] tiles) {
    this.tiles = tiles;
  }

  public Tile[] getTiles() {
    return tiles;
  }

  public void setTiles(Tile[] tiles) {
    this.tiles = tiles;
  }
}
