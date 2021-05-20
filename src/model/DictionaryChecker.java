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

  public ArrayList<String> getPlayableWords(String keyWord){
    return null;
  }


  /**
  public static void main(String [] args) {
    DictionaryChecker d = new DictionaryChecker();

    System.out.println(d.contains("AA"));
    System.out.println(d.dictionary.size());
    System.out.println(d.dictionary.keysThatMatch("..."+"VOGEL"+"."));

  }
   */
}
