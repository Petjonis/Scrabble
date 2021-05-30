/**
 * Class represents a bag of tiles which has a maximum of 100 pieces.
 *
 * @author fpetek, fjaehrli
 * @version 2.0
 */

package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import settings.GlobalSettings;

/**
 * The {@code TileBag} class represents the tile bag with methods to draw from,
 * or add tiles to the bag.
 *
 * @author fjaehrli
 */
public class TileBag {
  private static Random random = new Random();
  private final ArrayList<Tile> tileBag = new ArrayList<Tile>();

  /**
   * Initializes the {@code ArrayList<Tile> tileBag} with 100 letters.
   * The occurrence and the value distribution of letters is given in the
   * {@code letterValueDistribution.csv}
   */
  public TileBag() {
    try (FileReader fileReader =
            new FileReader(GlobalSettings.filepath + "letterValueDistribution.csv");
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] csvLine = line.split(",");
        int count = Integer.parseInt(csvLine[1]);
        for (int i = 0; i < count; i++) {
          char letter = csvLine[0].equalsIgnoreCase("blank") ? ' ' : csvLine[0].charAt(0);
          int value = Integer.parseInt(csvLine[2]);
          tileBag.add(new Tile(letter, value, null));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the tile bag
   *
    * @return {code ArrayList<Tile> tileBag}
   */
  public ArrayList<Tile> getTiles() {
    return tileBag;
  }

  /**
   * Removes a random letter from the bag
   *
   * @return random letter
   */
  public Tile drawLetter() {
    int randomIndex = random.nextInt(tileBag.size());
    return tileBag.remove(randomIndex);
  }

  /**
   * Adds an array of tiles to the bag
   *
   * @param tiles {@code Tile[]} array
   */
  public void addTiles(Tile[] tiles) {
    for (Tile t : tiles) {
      this.tileBag.add(t);
    }
  }

  /**
   * Tile bag empty?
   *
   * @return {@code true} if empty and {@code false} otherwise
   */
  public boolean isEmpty() {
    return tileBag.isEmpty();
  }

  /**
   * Get the size (tiles remaining) of the tile bag
   *
   * @return {@code int} size
   */
  public int getSize() {
    return tileBag.size();
  }
}
