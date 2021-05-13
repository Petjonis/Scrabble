package model;

import settings.GlobalSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class represents the board of the game.
 *
 * @author
 * @version 1.0
 */
public class Board {

  private Square[][] squares = new Square[GlobalSettings.ROWS][GlobalSettings.COLUMNS];

  public Board() {
    initializeBoard();
    initializeSpecialSquares();
  }

  private void initializeBoard(){
    for(int i = 0; i<GlobalSettings.ROWS ;i++) {
      for (int j = 0; j < GlobalSettings.COLUMNS; j++) {
        squares[i][j] = new Square();
      }
    }
  }

  private void initializeSpecialSquares() {
    try (FileReader fileReader = new FileReader(GlobalSettings.filepath +"specialSquares.csv");
         BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] csvLine = line.split(",");
        int row = Integer.parseInt(csvLine[0]) + 7;
        int col = Integer.parseInt(csvLine[1]);
        String bonusType = csvLine[2].toLowerCase();
        Square s = new Square();
        s.setType(bonusType);
        flipsOf(row,col,s);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void flipsOf(int row, int col, Square s) {
    squares[row][col] = s;
    squares[row][GlobalSettings.COLUMNS - col - 1] = s;
    squares[GlobalSettings.ROWS - row - 1][col] = s;
    squares[GlobalSettings.ROWS - row - 1][GlobalSettings.COLUMNS - col - 1] = s;
  }

  public Square[][] getSquares() {
    return squares;
  }

  public void placeTile(Tile tile, int row, int col){
    squares[row][col].setTile(tile);
    squares[row][col].setOccupied();
  }

}
