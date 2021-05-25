package model;

import java.io.*;
import java.util.*;
import settings.GlobalSettings;

public class DictionaryChecker {


  /*
   * this dictionary has the size 26 for 26 letters. at each index we have different length due to
   * the amount of words per character
   */
  private TrieSET dictionary;

  public DictionaryChecker() {
    dictionary = new TrieSET();
    try (FileReader fileReader = new FileReader(GlobalSettings.filepath + "Collins Scrabble Words (2019) with definitions.txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      //Skip first 2 lines
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

  public boolean contains(String key){
    return dictionary.contains(key);
  }

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
