package model;

import settings.GlobalSettings;
import model.Tile;

public class Board {

  private Tile[][] squares = new Tile[GlobalSettings.ROWS][GlobalSettings.COLUMNS];
  private TileType tType;
  private boolean occupied = false;
  private SquareType sqType;

  public Board(boolean o, TileType tt, SquareType st) {
    this.occupied = o;
    this.tType = tt;
    this.sqType = st;
  }


  public void setOccupied() {
    this.occupied = true;
  }

}