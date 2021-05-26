/**
 * Class represents a tile piece
 *
 * @author fpetek
 * @version 1.0
 */
package model;

import java.io.Serializable;

public class Tile implements Serializable {
  private char letter;
  private int value;
  private Position pos;

  /**
   * Constructor for a tile piece.
   *
   * @param letter Is the letter
   * @param value Is value of tile
   */
  public Tile(char letter, int value, Position position) {
    this.letter = letter;
    this.value = value;
    this.pos = position;
  }

  public String toString() {
    return letter + "," + value;
  }

  public char getLetter() {
    return letter;
  }

  public void setLetter(char letter) {
    this.letter = letter;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getRow() {
    return pos.getRow();
  }

  public int getCol() {
    return pos.getCol();
  }
}
