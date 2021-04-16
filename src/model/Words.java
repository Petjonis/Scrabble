package model;

public class Words {

  private String word;
  private int value;

  private Position wordBeginning, wordEnding;

  public Words(String word, int value, Position beginning, Position ending) {
    this.setWord(word);
    this.setValue(value);
    this.wordBeginning = beginning;
    this.wordEnding = ending;

  }

  public Position getWordBeginning() {
    return wordBeginning;
  }

  public void setWordBeginning(Position wordBeginning) {
    this.wordBeginning = wordBeginning;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }


}
