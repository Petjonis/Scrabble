package model;

import model.Tile;
import settings.GlobalSettings;

/**
 * This class represents the board of the game.
 *
 * @author socho
 * @version 1.0
 */
public class Board {

  private Tile[][] squares = new Tile[GlobalSettings.ROWS][GlobalSettings.COLUMNS];
  private TileType tiType;
  private boolean occupied = false;
  private SquareType sqType;

  public Board(boolean o, TileType tt, SquareType st) {
    this.occupied = o;
    this.tiType = tt;
    this.sqType = st;
  }


  public void setOccupied() {
    this.occupied = true;
  }

}