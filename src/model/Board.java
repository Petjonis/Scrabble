package model;

import controller.GameInfoController;
import javafx.util.Pair;
import settings.GlobalSettings;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * This class represents the Board of the game and handles the checking of valid moves
 * with the dictionary checker.
 * If a tile gets dropped on the game board, it gets added to the {@code ArrayList<Tile> tilesPendingConfirmation}.
 * If the player presses the play button, the {@code playedWords()} method returns the valid moves,
 * or {@code null} otherwise.
 *
 * @author fjaehrli
 */
public class Board {

  private final Square[][] squares = new Square[GlobalSettings.ROWS][GlobalSettings.COLUMNS];
  private final ArrayList<Tile> tilesPendingConfirmation = new ArrayList<>();
  private DictionaryChecker dictionary;

  /**
   * Constructor for the board class.
   * Calls the {@code initializeBoard()} and {@code initializeSpecialSquares()} method.
   */
  public Board() {
    initializeBoard();
    initializeSpecialSquares();
  }

  /**
   * Initializes the Board with empty Squares.
   */
  private void initializeBoard() {
    for (int i = 0; i < GlobalSettings.ROWS; i++) {
      for (int j = 0; j < GlobalSettings.COLUMNS; j++) {
        squares[i][j] = new Square();
      }
    }
  }

  /**
   * Initializes the board with the special squares.
   * Reads the type and position of the special squares from a .csv file.
   */
  private void initializeSpecialSquares() {
    try (FileReader fileReader = new FileReader(GlobalSettings.filepath + "specialSquares.csv");
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] csvLine = line.split(",");
        int row = Integer.parseInt(csvLine[0]) + 7;
        int col = Integer.parseInt(csvLine[1]);
        String bonusType = csvLine[2].toLowerCase();
        flipsOf(row, col, bonusType);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Initializes the dictionary.
   */
  public void initializeDictionary() {
    this.dictionary = new DictionaryChecker();
  }

  /**
   * Flips one special square on the board 4 times to initialize the board symmetrical.
   *
   * @param row the row of the board
   * @param col the column of the board
   * @param bonusType the bonus type of the square.
   */
  private void flipsOf(int row, int col, String bonusType) {
    squares[row][col].setType(bonusType);
    squares[row][GlobalSettings.COLUMNS - col - 1].setType(bonusType);
    squares[GlobalSettings.ROWS - row - 1][col].setType(bonusType);
    squares[GlobalSettings.ROWS - row - 1][GlobalSettings.COLUMNS - col - 1].setType(bonusType);
  }

  /**
   * Returns the whole board array.
   *
   * @return the board which is represented by a 2d array of squares.
   */
  public Square[][] getSquares() {
    return squares;
  }

  /**
   * Place a tile on the bord
   *
   * @param letter the Letter of the tile
   * @param value the value of the tile
   * @param row the row of the tile on the board
   * @param col the column of the tile on the board
   */
  public void placeTile(char letter, int value, int row, int col) {
    Tile tile = new Tile(letter, value, new Position(row, col));
    squares[row][col].setTile(tile);
    squares[row][col].setOccupied(true);
  }

  /**
   * Removes a tile from the board
   *
   * @param row the row of the tile on the board
   * @param col the column of the tile on the board
   */
  private void removeTile(int row, int col) {
    squares[row][col].setTile(null);
    squares[row][col].setOccupied(false);
  }

  /**
   * Checks whether the tiles pending confirmation (tiles dropped on the board by a player)
   * form a valid word on the board. If so, the method returns an ArrayList with the valid words
   * and their corresponding score,
   * otherwise it returns null and the Player gets an error message.
   *
   * @return {@code newWord} of validated words and their corresponding score or {@code null}
   */
  public ArrayList<Pair<String, Integer>> playedWords() {
    if (tilesPendingConfirmation.isEmpty()) {
      printErrorToGaminController("No word played");
      return null;
    } else if (!squares[7][7].isOccupied()) {
      Boolean orientation = tilesPendingVertical();
      if (tilesPendingContains(7, 7) && orientation != null) {
        HashSet<String> hashSet = new HashSet<>();
        ArrayList<Pair<String, Integer>> newWords = new ArrayList<>();
        for (Tile t : tilesPendingConfirmation) {
          hashSet.add(getWordAndScore(t.getRow(), t.getCol(), orientation).getKey());
        }
        if (hashSet.size() != 1) {
          printErrorToGaminController("You need to play a single word");
          return null;
        }
        Pair<String, Integer> word =
            getWordAndScore(
                tilesPendingConfirmation.get(0).getRow(),
                tilesPendingConfirmation.get(0).getCol(),
                orientation);
        if (dictionary.contains(word.getKey())) {
          newWords.add(word);
          return newWords;
        }
        printErrorToGaminController(word.getKey() + " is not in the dictionary");
        return null;
      }
      printErrorToGaminController("First word needs to be in the center");
      return null;
    } else if (tilesPendingHaveNeighbors()) {
      ArrayList<Pair<String, Integer>> newWords = new ArrayList<>();
      Boolean orientation = tilesPendingVertical();
      if (orientation == null) {
        printErrorToGaminController("Word needs to be played vertical/horizontal");
        return null;
      }
      HashSet<String> hashSet = new HashSet<>();
      for (Tile t : tilesPendingConfirmation) {
        hashSet.add(getWordAndScore(t.getRow(), t.getCol(), orientation).getKey());
      }
      if (hashSet.size() != 1) {
        printErrorToGaminController("Words need to be connected");
        return null;
      }
      Pair<String, Integer> word;
      word =
          getWordAndScore(
              tilesPendingConfirmation.get(0).getRow(),
              tilesPendingConfirmation.get(0).getCol(),
              orientation);
      if (word.getKey().length() > 1) newWords.add(word);
      for (Tile t : tilesPendingConfirmation) {
        word = getWordAndScore(t.getRow(), t.getCol(), !orientation);
        if (word.getKey().length() > 1) newWords.add(word);
      }

      for (Pair<String, Integer> p : newWords) {
        if (!dictionary.contains(p.getKey())) {
          printErrorToGaminController(p.getKey() + " is not in the dictionary (After Start)");
          return null;
        }
      }
      return newWords;
    } else {
      printErrorToGaminController("Words need to connect to existing words");
      return null;
    }
  }

  /**
   * Checks if tiles pending were played vertical.
   *
   * @return {@code true} if the tiles were played vertical, {@code false} if they were played horizontal
   * and {@code null} otherwise.
   */
  private Boolean tilesPendingVertical() {
    HashSet<Integer> col = new HashSet<>();
    HashSet<Integer> row = new HashSet<>();
    for (Tile t : tilesPendingConfirmation) {
      col.add(t.getCol());
      row.add(t.getRow());
    }
    if ((col.size() == 1 && row.size() == 1) || (col.size() > 1 && row.size() == 1)) {
      return false;
    } else if (col.size() == 1 && row.size() > 1) {
      return true;
    } else {
      return null;
    }
  }

  /**
   * Shows an error Message to the player.
   *
   * @param error The string with the error description
   */
  public void printErrorToGaminController(String error){
    GameInfoController.gameInfoController.addSystemMessage(error);
  }

  /**
   * Adds all tiles pending confirmation to the board
   */
  private void addTilesPendingToBoard() {
    for (Tile t : tilesPendingConfirmation) {
      placeTile(t.getLetter(), t.getValue(), t.getRow(), t.getCol());
    }
  }

  /**
   * If the player drops a tile from his rack to the board, it gets added to
   * the tile pending confirmation list.
   *
   * @param letter the Letter of the tile
   * @param value the value of the tile
   * @param row the row of the tile on the board
   * @param col the column of the tile on the board
   */
  public void addTilePending(char letter, int value, int row, int col) {
    Tile tile = new Tile(letter, value, new Position(row, col));
    tilesPendingConfirmation.add(tile);
  }

  /**
   * Removes specific tile from the tiles pending confirmation
   *
   * @param row the row of the tile on the board
   * @param col the column of the tile on the board
   */
  public void removeTilePending(int row, int col) {
    tilesPendingConfirmation.removeIf(tile -> tile.getRow() == row && tile.getCol() == col);
  }

  /**
   * Removes the tiles pending confirmation from the game board
   */
  private void removeTilesPendingFromBoard() {
    for (Tile t : tilesPendingConfirmation) {
      removeTile(t.getRow(), t.getCol());
    }
  }

  /**
   * Checks if at least one played tile has a neighbor on the board
   *
   * @return {@code true} if tiles pending have neighbors on the board, {@code false} otherwise
   */
  private boolean tilesPendingHaveNeighbors() {
    for (Tile t : tilesPendingConfirmation) {
      if (hasNeighbor(t.getRow(), t.getCol())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if tiles pending contain a tile with the specified coordinates. Mainly used for checking
   * if the first move is made over the middle of the board.
   *
   * @param row the row of the tile on the board
   * @param col the column of the tile on the board
   * @return {@code true} if tile with the specified row and column is on the board, {@code false} otherwise.
   */
  private boolean tilesPendingContains(int row, int col) {
    for (Tile t : tilesPendingConfirmation) {
      if (t.getRow() == row && t.getCol() == col) return true;
    }
    return false;
  }

  /**
   * Checks if a tile on the board has a neighbor / is connected to another tile
   *
   * @param row the row of the tile on the board
   * @param col the column of the tile on the board
   * @return {@code true} if tile has neighbor, {@code false} otherwise.
   */
  private boolean hasNeighbor(int row, int col) {
    if (row > 0 && squares[row - 1][col].isOccupied()) {
      return true;
    } else if (row < 14 && squares[row + 1][col].isOccupied()) {
      return true;
    } else if (col > 0 && squares[row][col - 1].isOccupied()) {
      return true;
    } else return col < 14 && squares[row][col + 1].isOccupied();
  }

  /**
   * Gets the Index of the first letter from a played word.
   *
   * @param row the row of the tile on the board
   * @param col the column of the tile on the board
   * @param vertical is the word played vertical or horizontal?
   * @return {@code index} of the first letter of a word.
   */
  private int getWordStartIndex(int row, int col, boolean vertical) {
    int index = (vertical) ? row : col;
    for (int i = (vertical) ? row : col; i >= 0; i--) {
      if (squares[(vertical) ? i : row][(vertical) ? col : i].isOccupied()) {
        index = i;
      } else {
        break;
      }
    }
    return index;
  }

  /**
   * Get the String of a played word and the corresponding score.
   *
   * @param row the row of the first letter of a word
   * @param col the column of the first letter of a word
   * @param vertical is the word played vertical or horizontal?
   * @return {@code Pair<String,Integer>} with {@code String} of the word and {@code Integer} of the score.
   */
  private Pair<String, Integer> getWordAndScore(int row, int col, boolean vertical) {
    addTilesPendingToBoard();
    StringBuilder sb = new StringBuilder();
    int score = 0;
    int wordMultiplier = 1;
    int wordBeginning = getWordStartIndex(row, col, vertical);
    for (int i = wordBeginning; i < 15; i++) {
      if (squares[(vertical) ? i : row][(vertical) ? col : i].isOccupied()) {
        sb.append(getLetter((vertical) ? i : row, (vertical) ? col : i));
        if (tilesPendingContains((vertical) ? i : row, (vertical) ? col : i)) {
          switch (squares[(vertical) ? i : row][(vertical) ? col : i].getType()) {
            case START:
            case DOUBLE_WORD:
              wordMultiplier *= 2;
              score += squares[(vertical) ? i : row][(vertical) ? col : i].getTile().getValue();
              break;
            case NO_BONUS:
              score += squares[(vertical) ? i : row][(vertical) ? col : i].getTile().getValue();
              break;
            case TRIPLE_WORD:
              wordMultiplier *= 3;
              score += squares[(vertical) ? i : row][(vertical) ? col : i].getTile().getValue();
              break;
            case DOUBLE_LETTER:
              score +=
                  (squares[(vertical) ? i : row][(vertical) ? col : i].getTile().getValue() * 2);
              break;
            case TRIPLE_LETTER:
              score +=
                  (squares[(vertical) ? i : row][(vertical) ? col : i].getTile().getValue() * 3);
              break;
          }
        } else {
          score += squares[(vertical) ? i : row][(vertical) ? col : i].getTile().getValue();
        }
      } else {
        break;
      }
    }
    removeTilesPendingFromBoard();
    return new Pair<>(sb.toString(), score * wordMultiplier);
  }

  /**
   * Clears the tiles pending confirmation list.
   */
  public void clearTilesPending() {
    tilesPendingConfirmation.clear();
  }

  /**
   * Return the letter of a tile on the bord
   *
   * @param row the row of the tile on the board
   * @param col the column of the tile on the board
   * @return {@code char} of a tile
   */
  private char getLetter(int row, int col) {
    return squares[row][col].getTile().getLetter();
  }

  /**
   * Return the Array of tiles pending confirmation
   *
   * @return {@code ArrayList<Tile>} of tiles pending confirmation
   */
  public ArrayList<Tile> getTilesPendingConfirmation() {
    return tilesPendingConfirmation;
  }

  /* public static void main(String [] args) {
  Board b = new Board();
  b.initializeDictionary();
  b.addTilePending('H',1,7,7);
  b.addTilePending('E',1,8,7);
  b.addTilePending('L',1,9,7);
  b.addTilePending('L',1,10,7);
  b.addTilePending('O',1,11,7);

  ArrayList<Pair<String,Integer>> playedWords = b.playedWords();
  if(playedWords != null){
  b.addTilesPendingToBoard();
  b.tilesPendingConfirmation.clear();
  for(Pair<String,Integer> p : playedWords){
  System.out.println("Word: "+p.getKey()+", Score: "+p.getValue());
  }
  System.out.println();
  }

  b.addTilePending('L',1,11,8);
  b.addTilePending('D',1,11,9);

  playedWords = b.playedWords();
  if(playedWords != null){
  b.addTilesPendingToBoard();
  b.tilesPendingConfirmation.clear();
  for(Pair<String,Integer> p : playedWords){
  System.out.println("Word: "+p.getKey()+", Score: "+p.getValue());
  }
  System.out.println();
  }

  b.addTilePending('W', 1, 9,4);
  b.addTilePending('R', 1, 9,6);
  b.addTilePending('O', 1, 9,5);
  b.addTilePending('D', 1, 9,8);

  playedWords = b.playedWords();
  if(playedWords != null){
  b.addTilesPendingToBoard();
  b.tilesPendingConfirmation.clear();
  for(Pair<String,Integer> p : playedWords){
  System.out.println("Word: "+p.getKey()+", Score: "+p.getValue());
  }
  System.out.println();
  }

  b.addTilePending('I', 1, 10,9);
  b.addTilePending('S', 1, 9,9);
  b.addTilePending('E', 1, 12,9);

  playedWords = b.playedWords();
  if(playedWords != null){
  b.addTilesPendingToBoard();
  b.tilesPendingConfirmation.clear();
  for(Pair<String,Integer> p : playedWords){
  System.out.println("Word: "+p.getKey()+", Score: "+p.getValue());
  }
  System.out.println();
  }

  }*/

}
