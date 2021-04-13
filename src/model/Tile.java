package model;

/**
 * Class represents a tile piece
 *
 * @author fpetek
 * @version 1.0
 */
public class Tile {
  private char letter;
  private int value;
  private TileType type;

  public Tile(char letter, int value, TileType type) {
    this.letter = letter;
    this.value = value;
    this.type = type;
  }

  /**
   * Method to create hundred tiles with classic letter and value distribution.
   * 0 points: BLANK ×2
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
    while (count < 100) {
      if (count < 12) {
        newTiles[count] = new Tile('E', 1, TileType.LETTER);
        count++;
      } else if (count >= 12 && count < 21) {
        newTiles[count] = new Tile('A', 1, TileType.LETTER);
        count++;
      } else if (count >= 21 && count < 30) {
        newTiles[count] = new Tile('I', 1, TileType.LETTER);
        count++;
      } else if (count >= 30 && count < 38) {
        newTiles[count] = new Tile('O', 1, TileType.LETTER);
        count++;
      } else if (count >= 38 && count < 44) {
        newTiles[count] = new Tile('N', 1, TileType.LETTER);
        count++;
      } else if (count >= 44 && count < 50) {
        newTiles[count] = new Tile('R', 1, TileType.LETTER);
        count++;
      } else if (count >= 50 && count < 56) {
        newTiles[count] = new Tile('T', 1, TileType.LETTER);
        count++;
      } else if (count >= 56 && count < 60) {
        newTiles[count] = new Tile('L', 1, TileType.LETTER);
        count++;
      } else if (count >= 60 && count < 64) {
        newTiles[count] = new Tile('S', 1, TileType.LETTER);
        count++;
      } else if (count >= 64 && count < 68) {
        newTiles[count] = new Tile('U', 1, TileType.LETTER);
        count++;
      } else if (count >= 68 && count < 72) {
        newTiles[count] = new Tile('D', 2, TileType.LETTER);
        count++;
      } else if (count >= 72 && count < 75) {
        newTiles[count] = new Tile('G', 2, TileType.LETTER);
        count++;
      } else if (count >= 75 && count < 77) {
        newTiles[count] = new Tile('B', 3, TileType.LETTER);
        count++;
      } else if (count >= 77 && count < 79) {
        newTiles[count] = new Tile('C', 3, TileType.LETTER);
        count++;
      } else if (count >= 79 && count < 81) {
        newTiles[count] = new Tile('M', 3, TileType.LETTER);
        count++;
      } else if (count >= 81 && count < 83) {
        newTiles[count] = new Tile('P', 3, TileType.LETTER);
        count++;
      } else if (count >= 83 && count < 85) {
        newTiles[count] = new Tile('F', 4, TileType.LETTER);
        count++;
      } else if (count >= 85 && count < 87) {
        newTiles[count] = new Tile('H', 4, TileType.LETTER);
        count++;
      } else if (count >= 87 && count < 89) {
        newTiles[count] = new Tile('V', 4, TileType.LETTER);
        count++;
      } else if (count >= 89 && count < 91) {
        newTiles[count] = new Tile('W', 4, TileType.LETTER);
        count++;
      } else if (count >= 91 && count < 93) {
        newTiles[count] = new Tile('Y', 4, TileType.LETTER);
        count++;
      } else if (count == 93) {
        newTiles[count] = new Tile('K', 5, TileType.LETTER);
        count++;
      } else if (count == 94) {
        newTiles[count] = new Tile('J', 8, TileType.LETTER);
        count++;
      } else if (count == 95) {
        newTiles[count] = new Tile('X', 8, TileType.LETTER);
        count++;
      } else if (count == 96) {
        newTiles[count] = new Tile('Q', 10, TileType.LETTER);
        count++;
      } else if (count == 97) {
        newTiles[count] = new Tile('Z', 10, TileType.LETTER);
        count++;
      } else if (count == 98) {
        newTiles[count] = new Tile(' ', 10, TileType.BLANK);
        count++;
      } else if (count == 99) {
        newTiles[count] = new Tile(' ', 10, TileType.BLANK);
        count++;
      }
    }
    return newTiles;
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
