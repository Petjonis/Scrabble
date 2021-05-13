/**
 * Class represents a tile piece
 *
 * @author fpetek
 * @version 1.0
 */

package model;

public class Tile {
  private char letter;
  private int value;

  /**
   * Constructor for a tile piece.
   *
   * @param letter Is the letter
   * @param value Is value of tile
   */
  public Tile(char letter, int value) {
    this.letter = letter;
    this.value = value;
  }

  public String toString(){
    return letter+","+value;
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




}
