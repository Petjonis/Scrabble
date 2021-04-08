package db;

import java.sql.*;

/**
 * This class contains all necessary methods to communicate with database for given context.
 *
 * @author fpetek
 * @version 1.0
 */
public class Database {
  private static final String filepath =
      System.getProperty("user.dir")
          + System.getProperty("file.separator")
          + "resources"
          + System.getProperty("file.separator")
          + "scrabble.db";
  protected Connection connection;
  protected PreparedStatement pstmt;
  protected ResultSet rs;
  private String sql;

  public Database() {}

  /**
   * Establishes connection to database and saves file to the dedicated resources folder with system
   * independent source path.
   */
  public void connect() {
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
      System.out.println("Connected to DB");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** Closes connection to database */
  public void disconnect() {
    try {
      this.connection.close();
      System.out.println("Disconnected DB");
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * Method to create the only table in the database to save user profiles. Won't create new table
   * if it already exists
   */
  public void createUserTable() {
    try (Statement statement = connection.createStatement()) {
      sql =
          "CREATE TABLE IF NOT EXISTS Users("
              + "ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
              + "Name TEXT NOT NULL, "
              + "Password TEXT NOT NULL, "
              + "Image TEXT NOT NULL, "
              + "Games INTEGER NOT NULL, "
              + "Wins INTEGER NOT NULL, "
              + "Loses INTEGER NOT NULL, "
              + "Score INTEGER NOT NULL)";
      statement.executeUpdate(sql);
      statement.close();
      System.out.println("User Table created!");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to drop/delete table with given name
   *
   * @param name of table
   */
  public void dropTable(String name) {
    try (Statement statement = connection.createStatement()) {
      sql = "DROP TABLE IF EXISTS " + name;
      statement.executeUpdate(sql);
      System.out.println("Dropped Table -> " + name);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * Method to add a user to existing table in database. Sets non-parameter values to Integer = 0,
   * String = "".
   *
   * @param name
   * @param password
   * @return returns true if user was added to database, returns false if user was not added to
   *     database
   */
  public boolean addUser(String name, String password) {
    try {
      sql =
          "INSERT INTO Users (Name, Password, Image, Games, Wins, Loses, Score) VALUES (?,?,?,?,?,?,?);";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, name);
      pstmt.setString(2, password);
      pstmt.setString(3, "");
      pstmt.setInt(4, 0);
      pstmt.setInt(5, 0);
      pstmt.setInt(6, 0);
      pstmt.setInt(7, 0);
      pstmt.executeUpdate();
      System.out.println("Added User -> " + name + " successfully!");
      pstmt.close();
      return true;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not add user -> " + name + "");
      return false;
    }
  }

  /**
   * Method to delete an existing user from database
   *
   * @param name
   * @return returns true if user was deleted successfully, returns false if user was not deleted
   *     successfully
   */
  public boolean deleteUser(String name) {
    try {
      sql = "DELETE FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, name);
      pstmt.executeUpdate();
      System.out.println("Deleted User -> " + name + " successfully!");
      pstmt.close();
      return true;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not delete user -> " + name + "");
      return false;
    }
  }

  /**
   * Method to change a username of an existing user *
   *
   * @param username
   * @param newUsername
   * @return returns true if username was updated successfully, returns false if username was not
   *     updated successfully
   */
  public boolean changeUsername(String username, String newUsername) {
    try {
      sql = "UPDATE Users SET Name = ? WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, newUsername);
      pstmt.setString(2, username);
      pstmt.executeUpdate();
      System.out.println(username + "changed username to -> " + newUsername + " successfully!");
      pstmt.close();
      return true;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not change username -> " + username + " to " + newUsername);
      return false;
    }
  }

  /**
   * Method to change password of an existing user.
   *
   * @param username
   * @param newPassword
   * @return returns true if password was changed successfully, returns false if password was not
   *     changed successfully
   */
  public boolean changePassword(String username, String newPassword) {
    try {
      sql = "UPDATE Users SET Password = ? WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, newPassword);
      pstmt.setString(2, username);
      pstmt.executeUpdate();
      System.out.println(username + " changed password successfully!");
      pstmt.close();
      return true;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not change password of -> " + username);
      return false;
    }
  }

  /**
   * Method to change image of an existing user. Doesn't check if filepath is valid!
   *
   * @param username
   * @param image is path to image
   * @return returns true if image was updated, returns false if image was not updated
   */
  public boolean changeImage(String username, String image) {
    try {
      sql = "UPDATE Users SET Image = ? WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, image);
      pstmt.setString(2, username);
      pstmt.executeUpdate();
      System.out.println(username + " changed image successfully!");
      pstmt.close();
      return true;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not change image of -> " + username);
      return false;
    }
  }

  /**
   * Method to check if a user already exists.
   *
   * @param username
   * @return returns true if user already exists, returns false if username is already taken
   */
  public boolean userExists(String username) {
    try {
      sql = "SELECT * FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        if (rs.getString("Name").equals(username)) {
          System.out.println("Username -> " + username + " already exists!");
          return true;
        }
      }
      System.out.println("Username -> " + username + " not taken!");
      return false;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Something went wrong");
      return true;
    }
  }

  /**
   * Method to get user id of user.
   *
   * @param username
   * @return returns -1 if user doesn't exist
   */
  public int getId(String username) {
    int id = -1;
    try {
      sql = "SELECT ID FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        id = rs.getInt("ID");
      }
      return id;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return id;
    }
  }

  /**
   * Method to get the password of a user.
   *
   * @param username
   * @return returns empty string if user doesn't exist
   */
  public String getPassword(String username) {
    String pw = "";
    try {
      sql = "SELECT Password FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        pw = rs.getString("Password");
      }
      return pw;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return pw;
    }
  }

  /**
   * Method to get the amount points of user
   *
   * @param username
   * @return returns -1 if user doesn't exist
   */
  public int getScore(String username) {
    int points = -1;
    try {
      sql = "SELECT Score FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        points = rs.getInt("Score");
      }
      return points;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return points;
    }
  }

  /**
   * Method to get amount of wins of a user
   *
   * @param username
   * @return returns -1 if user doesn't exist
   */
  public int getWins(String username) {
    int wins = -1;
    try {
      sql = "SELECT Wins FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        wins = rs.getInt("Wins");
      }
      return wins;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return wins;
    }
  }

  /**
   * Method to get amount of loses of a user
   *
   * @param username
   * @return returns -1 if user doesn't exist
   */
  public int getLoses(String username) {
    int loses = -1;
    try {
      sql = "SELECT Loses FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        loses = rs.getInt("Loses");
      }
      return loses;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return loses;
    }
  }

  /**
   * Method to get amount of games played of a user
   *
   * @param username
   * @return returns -1 if user doesn't exist
   */
  public int getGames(String username) {
    int games = -1;
    try {
      sql = "SELECT Games FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        games = rs.getInt("Games");
      }
      return games;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return games;
    }
  }

  /**
   * Method to update current points of a game to database
   *
   * @param username
   * @param currentScore which is old score plus additional gained points through the new word
   */
  public void updateScore(String username, int currentScore) {
    try {
      sql = "UPDATE Users SET Score = ? WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, currentScore);
      pstmt.setString(2, username);
      pstmt.executeUpdate();
      System.out.println("Score updated");
      pstmt.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not update Score");
    }
  }

  /**
   * Method to increment amount of wins and total games played
   *
   * @param username
   */
  public void incrementWins(String username) {
    try {
      sql = "UPDATE Users SET Wins = Wins +1, Games = Games +1 WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      pstmt.executeUpdate();
      System.out.println("Wins incremented");
      pstmt.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not update Wins");
    }
  }

  /**
   * Method to increment amount of loses and total games played
   *
   * @param username
   */
  public void incrementLoses(String username) {
    try {
      sql = "UPDATE Users SET Loses = Loses +1, Games = Games +1 WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      pstmt.executeUpdate();
      System.out.println("Loses incremented");
      pstmt.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not update Loses");
    }
  }

  /**
   * Method to reset points of a user after a game ends
   *
   * @param username
   */
  public void resetScore(String username) {
    try {
      sql = "UPDATE Users SET Score = ? WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, 0);
      pstmt.setString(2, username);
      pstmt.executeUpdate();
      System.out.println("Score reset successful");
      pstmt.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Could not update Score");
    }
  }
}
