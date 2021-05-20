/**
 * Class represents a bag of tiles which has a maximum of 100 pieces.
 *
 * @author fpetek, fjaehrli
 * @version 2.0
 */

package model;

import settings.GlobalSettings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileBag {
  private static Random random = new Random();
  private final ArrayList<Tile> tileBag = new ArrayList<Tile>();

  public TileBag() {
    try (FileReader fileReader = new FileReader(GlobalSettings.filepath +"letterValueDistribution.csv");
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

  public ArrayList<Tile> getTiles() {
    return tileBag;
  }

  public Tile drawLetter() {
    int randomIndex = random.nextInt(tileBag.size());
    return tileBag.remove(randomIndex);
  }

  public void addTiles(Tile[] tiles){
    for(Tile t : tiles){
      this.tileBag.add(t);
    }
  }

  public boolean isEmpty(){
    return tileBag.isEmpty();
  }

}
