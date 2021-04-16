package model;

import model.Tile;
import settings.GlobalSettings;

/**
 * This class represents the board of the game.
 *
 * @author
 * @version 1.0
 */
public class Board {

  private Square[][] squares = new Square[GlobalSettings.ROWS][GlobalSettings.COLUMNS];
  private TileType tiType;
  private boolean occupied = false;

  public Board(boolean occupied, TileType tileType) {
    this.occupied = occupied;
    this.tiType = tileType;
  }
  
  

  public void initializeBoard() {

    for (int i = 0; i < GlobalSettings.ROWS; i++) {
      for (int j = 0; j < GlobalSettings.COLUMNS; j++) {

        if (i == 0 && j == 14 || i == 14 && j == 14 || i == 14 && j == 0 || i == 0 && j == 0
            || i == 0 && j == 7 || i == 7 && j == 14 || i == 7 && j == 0 || i == 14 && j == 7) {
          Square oneSquare = new Square(SquareType.TRIPLE_WORD, null);
          squares[i][j] = oneSquare;
        } else if (i == 1 && j == 1 || i == 2 && j == 2 || i == 3 && j == 3 || i == 4 && j == 4 ||
            i == 10 && j == 10 || i == 11 && j == 11 || i == 12 && j == 12 || i == 13 && j == 13 ||
            i == 1 && j == 13 || i == 2 && j == 12 || i == 3 && j == 11 || i == 4 && j == 10 ||
            i == 13 && j == 1 || i == 12 && j == 2 || i == 11 && j == 3 || i == 10 && j == 4) {
          Square oneSquare = new Square(SquareType.DOUBLE_WORD, null);
          squares[i][j] = oneSquare;

        }
        else if(i == 0 && j == 3 || i == 0 && j == 11 || 
            i == 2 && j == 6 || i == 2 && j == 8 ||
            i == 3 && j == 0 || i == 3 && j == 7 || i == 3 && j == 14 ||
            i == 6 && j == 2 || i == 6 && j == 6 || i == 6 && j == 8 || i == 6 && j == 12 ||
            i == 7 && j == 3 || i == 7 && j == 11 ||
            i == 8 && j == 2 || i == 8 && j == 6 || i == 8 && j == 8 || i == 8 && j == 12 ||
            i == 11 && j == 0 || i == 11 && j == 7 || i == 11 && j == 14 ||
            i == 12 && j == 6 || i == 12 && j == 8 ||
            i == 0 && j == 3 || i == 0 && j == 11) {
          Square oneSquare =  new Square(SquareType.DOUBLE_LETTER, null);
          squares[i][j] = oneSquare;
        }
        else if(i == 1 && j == 5 || i == 1 && j == 9 || 
            i == 5 && j == 1 || i == 5 && j == 5 || i == 5 && j == 9 || i == 5 && j == 13 ||
            i == 9 && j == 1 || i == 9 && j == 5 || i == 9 && j == 9 || i == 9 && j == 13 ||
            i == 13 && j == 5 || i == 13 && j == 9) {
          
          Square oneSquare =  new Square(SquareType.TRIPLE_LETTER, null);
          squares[i][j] = oneSquare;
          
        }
        else if (i == 7 && j == 7) {
          Square oneSquare =  new Square(SquareType.START, null);
          squares[i][j] = oneSquare;
        }
        else {
          Square oneSquare = new Square(SquareType.NO_BONUS, null);
          squares[i][j] = oneSquare;

        }

      }
    }

  }


  public void setOccupied() {
    this.occupied = true;
  }

}
