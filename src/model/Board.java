package model;

import javafx.util.Pair;
import settings.GlobalSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * This class represents the board of the game.
 *
 * @author fjaehrli
 * @version 1.0
 */
public class Board {

    private final Square[][] squares = new Square[GlobalSettings.ROWS][GlobalSettings.COLUMNS];
    private final ArrayList<Tile> tilesPendingConfirmation = new ArrayList<>();
    private DictionaryChecker dictionary;

    public Board() {
        initializeBoard();
        initializeSpecialSquares();
    }

    private void initializeBoard() {
        for (int i = 0; i < GlobalSettings.ROWS; i++) {
            for (int j = 0; j < GlobalSettings.COLUMNS; j++) {
                squares[i][j] = new Square();
            }
        }
    }

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

    public void initializeDictionary(){
        this.dictionary = new DictionaryChecker();
    }

    private void flipsOf(int row, int col, String bonusType) {
        squares[row][col].setType(bonusType);
        squares[row][GlobalSettings.COLUMNS - col - 1].setType(bonusType);
        squares[GlobalSettings.ROWS - row - 1][col].setType(bonusType);
        squares[GlobalSettings.ROWS - row - 1][GlobalSettings.COLUMNS - col - 1].setType(bonusType);
    }

    public Square[][] getSquares() {
        return squares;
    }

    public void placeTile(char letter, int value, int row, int col) {
        Tile tile = new Tile(letter, value, new Position(row, col));
        squares[row][col].setTile(tile);
        squares[row][col].setOccupied(true);
    }

    private void removeTile(int row, int col) {
        squares[row][col].setTile(null);
        squares[row][col].setOccupied(false);
    }

    public ArrayList<Pair<String, Integer>> playedWords() {
        if (tilesPendingConfirmation.isEmpty()) {
            System.out.println("no Word Played");
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
                    System.out.println("You need to Play a single Word");
                    return null;
                }
                Pair<String, Integer> word = getWordAndScore(tilesPendingConfirmation.get(0).getRow(),
                        tilesPendingConfirmation.get(0).getCol(), orientation);
                if (dictionary.contains(word.getKey())) {
                    newWords.add(word);
                    return newWords;
                }
                System.out.println(word.getKey() + " is not in the Dictionary");
                return null;
            }
            System.out.println("First Word needs to be in the Center");
            return null;
        } else if (tilesPendingHaveNeighbors()) {
            ArrayList<Pair<String, Integer>> newWords = new ArrayList<>();
            Boolean orientation = tilesPendingVertical();
            if (orientation == null) {
                System.out.println("Word needs to be played vertical/horizontal");
                return null;
            }
            HashSet<String> hashSet = new HashSet<>();
            for (Tile t : tilesPendingConfirmation) {
                hashSet.add(getWordAndScore(t.getRow(), t.getCol(), orientation).getKey());
            }
            if (hashSet.size() != 1) {
                System.out.println("Words need to be connected");
                return null;
            }
            Pair<String, Integer> word;
            word = getWordAndScore(tilesPendingConfirmation.get(0).getRow(),
                    tilesPendingConfirmation.get(0).getCol(), orientation);
            if (word.getKey().length() > 1) newWords.add(word);
            for (Tile t : tilesPendingConfirmation) {
                word = getWordAndScore(t.getRow(), t.getCol(), !orientation);
                if (word.getKey().length() > 1) newWords.add(word);
            }

            for (Pair<String, Integer> p : newWords) {
                if (!dictionary.contains(p.getKey())) {
                    System.out.println(p.getKey() + " is not in the Dictionary (After Start)");
                    return null;
                }
            }
            return newWords;
        } else {
            System.out.println("Words need to connect to existing words");
            return null;
        }
    }

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

    private void addTilesPendingToBoard() {
        for (Tile t : tilesPendingConfirmation) {
            placeTile(t.getLetter(), t.getValue(), t.getRow(), t.getCol());
        }
    }

    public void addTilePending(char letter, int value, int row, int col) {
        Tile tile = new Tile(letter, value, new Position(row, col));
        tilesPendingConfirmation.add(tile);
    }

    public void removeTilePending(int row, int col) {
      tilesPendingConfirmation.removeIf(tile -> tile.getRow() == row && tile.getCol() == col);
    }

    private void removeTilesPendingFromBoard() {
        for (Tile t : tilesPendingConfirmation) {
            removeTile(t.getRow(), t.getCol());
        }
    }

    private boolean tilesPendingHaveNeighbors() {
        for (Tile t : tilesPendingConfirmation) {
            if (hasNeighbor(t.getRow(), t.getCol())) {
                return true;
            }
        }
        return false;
    }

    private boolean tilesPendingContains(int row, int col) {
        for (Tile t : tilesPendingConfirmation) {
            if (t.getRow() == row && t.getCol() == col) return true;
        }
        return false;
    }

    private boolean hasNeighbor(int row, int col) {
        if (row > 0 && squares[row - 1][col].isOccupied()) {
            return true;
        } else if (row < 14 && squares[row + 1][col].isOccupied()) {
            return true;
        } else if (col > 0 && squares[row][col - 1].isOccupied()) {
            return true;
        } else return col < 14 && squares[row][col + 1].isOccupied();
    }

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
                            score += (squares[(vertical) ? i : row][(vertical) ? col : i].getTile().getValue() * 2);
                            break;
                        case TRIPLE_LETTER:
                            score += (squares[(vertical) ? i : row][(vertical) ? col : i].getTile().getValue() * 3);
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

    public void clearTilesPending() {
        tilesPendingConfirmation.clear();
    }

    private char getLetter(int row, int col) {
        return squares[row][col].getTile().getLetter();
    }

    public ArrayList<Tile> getTilesPendingConfirmation() {
        return tilesPendingConfirmation;
    }

    /*
     public static void main(String [] args) {
     Board b = new Board();
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


     }

     */


}
