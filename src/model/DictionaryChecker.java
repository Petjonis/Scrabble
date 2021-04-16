package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import settings.GlobalSettings;

public class DictionaryChecker {


  /*
   * this dictionary has the size 26 for 26 letters. at each index we have different length due to
   * the amount of words per character
   */
  private static ArrayList<ArrayList<String>> dictionary = new ArrayList<ArrayList<String>>();

  public static void initializeDictionaryFromFile(String filePath) {

    for (int i = 0; i < 26; i++) {
      dictionary.add(new ArrayList<String>());
    }

    try {
      File myObj = new File(filePath);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        data = data.split("\t")[0];

        char startingChar = data.charAt(0);
        int charIndex = startingChar - 65;

        dictionary.get(charIndex).add(data);


      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }


  }

  public static boolean checkWordExistence(String word) {
    
    word = word.toUpperCase();

    char startingChar = word.charAt(0);
    int charIndex = startingChar - 65;

    boolean wordExists = false;

    ArrayList<String> listForChecking = dictionary.get(charIndex);

    for (int i = 0; i < listForChecking.size(); i++) {

      if (word.equals(listForChecking.get(i))) {
        wordExists = true;
        break;
      }

    }
    
    return wordExists;



  }

  public static void main(String[] args) {
    String path = GlobalSettings.filepath + "ScrabbleWords.txt";
    initializeDictionaryFromFile(path);

    System.out.println(checkWordExistence("eggsalad"));
    System.out.println(checkWordExistence("eggsalad"));

  }



}
