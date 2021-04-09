package model;

/**
 * Class represents a tile piece
 *
 * @author fpetek
 * @version 1.0
 */
public class Tile {
  private char letter;
  private final int value;
  private final TileType type;

  public Tile(char letter, int value, TileType type) {
    this.letter = letter;
    this.value = value;
    this.type = type;
  }

  /**
   * Method to create hundred tiles with classic letter and value distribution.
   * 2 blank tiles (scoring 0points)
   * 1 point: E ×12, A ×9, I ×9, O ×8, N ×6, R ×6, T ×6, L ×4, S ×4, U ×4
   * 2 points: D ×4, G ×3
   * 3 points: B ×2, C ×2, M ×2, P ×2
   * 4 points: F ×2, H ×2, V ×2, W ×2, Y ×2
   * 5 points: K ×1
   * 8 points: J ×1, X ×1
   * 10 points: Q ×1, Z ×1
   *
   * @return Returns a tile array with hundred tiles.
   */
  public static Tile[] createClassicTiles() {
    Tile[] newTiles = new Tile[100];
    int count = 0;
    while (count < 12) {
      newTiles[count] = new Tile('E', 1, TileType.LETTER);
      count++;
    }
    while (count < 21) {
      newTiles[count] = new Tile('A', 1, TileType.LETTER);
      count++;
    }
    while (count < 30) {
      newTiles[count] = new Tile('I', 1, TileType.LETTER);
      count++;
    }
    while (count < 38) {
      newTiles[count] = new Tile('O', 1, TileType.LETTER);
      count++;
    }
    while (count < 44) {
      newTiles[count] = new Tile('N', 1, TileType.LETTER);
      count++;
    }
    while (count < 50) {
      newTiles[count] = new Tile('R', 1, TileType.LETTER);
      count++;
    }
    while (count < 56) {
      newTiles[count] = new Tile('T', 1, TileType.LETTER);
      count++;
    }
    while (count < 60) {
      newTiles[count] = new Tile('L', 1, TileType.LETTER);
      count++;
    }
    while (count < 64) {
      newTiles[count] = new Tile('S', 1, TileType.LETTER);
      count++;
    }
    while (count < 68) {
      newTiles[count] = new Tile('U', 1, TileType.LETTER);
      count++;
    }
    while (count < 72) {
      newTiles[count] = new Tile('D', 2, TileType.LETTER);
      count++;
    }
    while (count < 75) {
      newTiles[count] = new Tile('G', 2, TileType.LETTER);
      count++;
    }
    while (count < 77) {
      newTiles[count] = new Tile('B', 3, TileType.LETTER);
      count++;
    }
    while (count < 79) {
      newTiles[count] = new Tile('C', 3, TileType.LETTER);
      count++;
    }
    while (count < 81) {
      newTiles[count] = new Tile('M', 3, TileType.LETTER);
      count++;
    }
    while (count < 83) {
      newTiles[count] = new Tile('P', 3, TileType.LETTER);
      count++;
    }
    while (count < 85) {
      newTiles[count] = new Tile('F', 4, TileType.LETTER);
      count++;
    }
    while (count < 87) {
      newTiles[count] = new Tile('H', 4, TileType.LETTER);
      count++;
    }
    while (count < 89) {
      newTiles[count] = new Tile('V', 4, TileType.LETTER);
      count++;
    }
    while (count < 91) {
      newTiles[count] = new Tile('W', 4, TileType.LETTER);
      count++;
    }
    while (count < 93) {
      newTiles[count] = new Tile('Y', 4, TileType.LETTER);
      count++;
    }
    while (count < 95) {
      newTiles[count] = new Tile('K', 5, TileType.LETTER);
      count++;
    }
    while (count < 96) {
      newTiles[count] = new Tile('J', 8, TileType.LETTER);
      count++;
    }
    while (count < 97) {
      newTiles[count] = new Tile('X', 8, TileType.LETTER);
      count++;
    }
    while (count < 98) {
      newTiles[count] = new Tile('Q', 10, TileType.LETTER);
      count++;
    }
    while (count < 99) {
      newTiles[count] = new Tile('Z', 10, TileType.LETTER);
      count++;
    }
    while (count <= 100) {
      newTiles[count] = new Tile(' ', 10, TileType.BLANK);
      count++;
    }

    return newTiles;
  }

  /**
   * Method to set the letter on the blank tile. Checks if letter is blank or not. Throws no
   * NullPointerException!
   *
   * @param letter is the letter to set
   */
  public void setBlankLetter(char letter) {
    if (this.type == TileType.BLANK) {
      this.letter = letter;
    }
  }
}
