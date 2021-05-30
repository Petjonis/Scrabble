package model;

import settings.GlobalSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class holds a {@code TrieSET} which represents an ordered set of strings, containing all the
 * words of the Collins Scrabble Words (2019) with definitions.txt file.
 * It provides two methods, {@code contains()} and {@code isAnagram()}
 *
 * @author fjaehrli
 */
public class DictionaryChecker {

  private TrieSET dictionary;

  /**
   * Initializes the dictionary with words.
   */
  public DictionaryChecker() {
    dictionary = new TrieSET();
    try (FileReader fileReader =
            new FileReader(
                GlobalSettings.filepath + "Collins Scrabble Words (2019) with definitions.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      // Skip first 2 lines
      bufferedReader.readLine();
      bufferedReader.readLine();
      while ((line = bufferedReader.readLine()) != null) {
        String[] s = line.split("\\s");
        String word = s[0];
        dictionary.add(word);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Does the set contain the given key?
   *
   * @param key the key
   * @return {@code true} if the set contains {@code key} and {@code false} otherwise
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public boolean contains(String key) {
    return dictionary.contains(key);
  }

  /**
   * Are two words an anagram?
   *
   * @param s1 first string
   * @param s2 second string
   * @return {@code true} if strings are anagram and {@code false} otherwise
   */
  public boolean isAnagram(String s1, String s2) {
    if (s1.length() != s2.length()) {
      return false;
    }
    int count[] = new int[256];
    for (int i = 0; i < s1.length(); i++) {
      count[s1.charAt(i)]++;
      count[s2.charAt(i)]--;
    }
    for (int i = 0; i < 256; i++) {
      if (count[i] != 0) {
        return false;
      }
    }
    return true;
  }

  /* public static void main(String [] args) {
    DictionaryChecker d = new DictionaryChecker();

    char[] keys = new char[15];
    keys[5] = 'W';
    keys[6] = 'O';
    keys[7] = 'R';
    keys[8] = 'L';
    keys[9] = 'D';
    keys[11] = 'X';
    d.getPlayableWords(keys);
   System.out.println(d.dictionary.keysThatMatch(".......WORLD"));
    d.isAnagram()
  }*/

}
