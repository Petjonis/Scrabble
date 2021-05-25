/**
 * This class contains all necessary methods to communicate with database for given context.
 *
 * @author fpetek
 * @version 1.1
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import settings.GlobalSettings;
import utility.Hashing;

public class Database {
  private Connection connection;
  private PreparedStatement pstmt;
  private ResultSet rs;
  private String sql;

  public Database() {}

  /**
   * Establishes connection to database and saves file to the dedicated resources folder with system
   * independent source path.
   */
  public void connect() {
    try {
      Class.forName("org.sqlite.JDBC");
      connection =
          DriverManager.getConnection("jdbc:sqlite:" + GlobalSettings.filepath + "scrabble.db");
      System.out.println("Connected to DB");
    } catch (ClassNotFoundException e) {
      System.out.println("Could not find the database driver " + e.getMessage());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** Closes connection to database. */
  public void disconnect() {
    try {
      this.connection.close();
      System.out.println("Disconnected DB");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to create the only table in the database to save user profiles. Won't create new table
   * if it already exists.
   */
  public void createUserTable() {
    try (Statement statement = connection.createStatement()) {
      sql =
          "CREATE TABLE IF NOT EXISTS Users("
              + "ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
              + "Name TEXT NOT NULL UNIQUE , "
              + "Password TEXT NOT NULL, "
              + "Image TEXT NOT NULL, "
              + "Games INTEGER NOT NULL, "
              + "Wins INTEGER NOT NULL, "
              + "Loses INTEGER NOT NULL, "
              + "Score INTEGER NOT NULL, "
              + "Salt TEXT NOT NULL)";
      statement.executeUpdate(sql);
      statement.close();
      System.out.println("User Table created!");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to drop/delete table with given name.
   *
   * @param name of table.
   */
  public void dropTable(String name) {
    try (Statement statement = connection.createStatement()) {
      sql = "DROP TABLE IF EXISTS " + name;
      statement.executeUpdate(sql);
      System.out.println("Dropped Table -> " + name);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to add a user to existing table in database. Sets non-parameter values to Integer = 0,
   * String = "".
   *
   * @param username is name of user.
   * @param password is password of user.
   * @return Returns true if user was added to database, returns false if user was not added to
   *     database.
   */
  public boolean addUser(String username, String password) {
    byte[] salt = Hashing.generateSalt();
    try {
      sql =
          "INSERT INTO Users ("
              + "Name, "
              + "Password, "
              + "Image, "
              + "Games, "
              + "Wins, "
              + "Loses, "
              + "Score,"
              + "Salt) "
              + "VALUES (?,?,?,?,?,?,?,?);";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      pstmt.setString(2, Hashing.generateHash(password, salt));
      pstmt.setString(3, "");
      pstmt.setInt(4, 0);
      pstmt.setInt(5, 0);
      pstmt.setInt(6, 0);
      pstmt.setInt(7, 0);
      pstmt.setString(8, Hashing.bytesToString(salt));
      pstmt.executeUpdate();
      pstmt.close();
      System.out.println("Added User -> " + username + " successfully!");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Could not add user -> " + username);
    return false;
  }

  /**
   * Method to delete an existing user from database.
   *
   * @param username is name of user.
   * @return Returns true if user was deleted successfully, returns false if user was not deleted
   *     successfully.
   */
  public boolean deleteUser(String username) {
    try {
      sql = "DELETE FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      pstmt.executeUpdate();
      pstmt.close();
      System.out.println("Deleted User -> " + username + " successfully!");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Could not delete user -> " + username + "");
    return false;
  }

  /**
   * Method to change a username of an existing user.
   *
   * @param username is name of user.
   * @param newUsername is new name of user.
   * @return Returns true if username was updated successfully, returns false if username was not
   *     updated successfully.
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Could not change username -> " + username + " to " + newUsername);
    return false;
  }

  /**
   * Method to change password of an existing user.
   *
   * @param username is name of user.
   * @param newPassword is new password of user.
   * @return Returns true if password was changed successfully, returns false if password was not
   *     changed successfully.
   */
  public boolean changePassword(String username, String newPassword) {
    byte[] salt = Hashing.generateSalt();
    try {
      sql = "UPDATE Users SET Password = ?, Salt = ? WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, Hashing.generateHash(newPassword, salt));
      pstmt.setString(2, Hashing.bytesToString(salt));
      pstmt.setString(3, username);
      pstmt.executeUpdate();
      pstmt.close();
      System.out.println(username + " changed password successfully!");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Could not change password of -> " + username);
    return false;
  }

  /**
   * Method to change image of an existing user. Doesn't check if filepath is valid!
   *
   * @param username is name of user.
   * @param image is path to image.
   * @return Returns true if image was updated, returns false if image was not updated.
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Could not change image of -> " + username);
    return false;
  }

  /**
   * Method to check if a user already exists.
   *
   * @param username is name of user.
   * @return Returns true if user already exists, returns false if username is available.
   */
  public boolean userExists(String username) {
    try (Statement statement = connection.createStatement()) {
      sql = "SELECT Name FROM Users;";
      rs = statement.executeQuery(sql);
      while (rs.next()) {
        if (rs.getString("Name").equals(username)) {
          System.out.println("Username -> " + username + " already exists!");
          return true;
        }
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Username -> " + username + " available!");
    return false;
  }

  /**
   * Method to get user id of user.
   *
   * @param username is name of user.
   * @return Returns -1 if user doesn't exist.
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
      rs.close();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  /**
   * Method to get the password of a user.
   *
   * @param username is name of user.
   * @return Returns null if user does not exist.
   */
  public String getPassword(String username) {
    String pw = null;
    try {
      sql = "SELECT Password FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        pw = rs.getString("Password");
      }
      rs.close();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return pw;
  }

  /**
   * Method to get the salt of the salted and hashed password.
   *
   * @param username is name of user.
   * @return Returns null if user does not exist.
   */
  public String getSalt(String username) {
    String salt = null;
    try {
      sql = "SELECT Salt FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        salt = rs.getString("Salt");
      }
      rs.close();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return salt;
  }

  /**
   * Method to get the amount points of user.
   *
   * @param username is name of user.
   * @return Returns -1 if user doesn't exist.
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
      rs.close();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return points;
  }

  /**
   * Method to get amount of wins of a user.
   *
   * @param username is name of user.
   * @return Returns -1 if user doesn't exist.
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
      rs.close();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return wins;
  }

  /**
   * Method to get amount of loses of a user.
   *
   * @param username is name of user.
   * @return Returns -1 if user doesn't exist.
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
      rs.close();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return loses;
  }

  /**
   * Method to get amount of games played of a user.
   *
   * @param username is name of user.
   * @return Returns -1 if user doesn't exist.
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
      rs.close();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return games;
  }

  /**
   * Method to update current points of a game to database.
   *
   * @param username is name of user.
   * @param currentScore which is old score plus additional gained points.
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to increment amount of wins and total games played.
   *
   * @param username is name of user.
   */
  public void incrementWins(String username) {
    try {
      sql = "UPDATE Users SET Wins = Wins +1, Games = Games +1 WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      pstmt.executeUpdate();
      System.out.println("Wins incremented");
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to increment amount of loses and total games played.
   *
   * @param username is name of user.
   */
  public void incrementLoses(String username) {
    try {
      sql = "UPDATE Users SET Loses = Loses +1, Games = Games +1 WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      pstmt.executeUpdate();
      System.out.println("Loses incremented");
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to reset points of a user after a game ends.
   *
   * @param username is name of user.
   */
  public void resetScore(String username) {
    try {
      sql = "UPDATE Users SET Score = ? WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, 0);
      pstmt.setString(2, username);
      pstmt.executeUpdate();
      pstmt.close();
      System.out.println("Score reset successful");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method checks if password string is the salted-hashed password in the database.
   *
   * @param username is name of user
   * @param password is password of user
   * @return Returns true if password is correct
   */
  public boolean checkPassword(String username, String password) {
    String dbPassword = null;
    String salt = null;
    try {
      sql = "SELECT Password, Salt FROM Users WHERE Name = ?;";
      pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, username);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        dbPassword = rs.getString("Password");
        salt = rs.getString("Salt");
      }
      rs.close();
      pstmt.close();
      if (Hashing.generateHash(password, Hashing.stringToBytes(salt)).equals(dbPassword)) {
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /** Method to print all database entries. Not formatted! */
  public void printDatabase() {
    try (Statement statement = connection.createStatement()) {
      sql = "SELECT * FROM Users";
      rs = statement.executeQuery(sql);
      while (rs.next()) {
        System.out.print("ID: " + rs.getString("ID") + " | ");
        System.out.print("Name: " + rs.getString("Name") + " | ");
        System.out.print("Password: " + rs.getString("Password") + " | ");
        System.out.print("Image: " + rs.getString("Image") + " | ");
        System.out.print("Games: " + rs.getString("Games") + " | ");
        System.out.print("Wins: " + rs.getString("Wins") + " | ");
        System.out.print("Loses: " + rs.getString("Loses") + " | ");
        System.out.print("Score: " + rs.getString("Score") + " | ");
        System.out.print("Salt: " + rs.getString("Salt") + " | ");
        System.out.println();
      }
      rs.close();
    } catch (java.sql.SQLException e) {
      e.printStackTrace();
    }
  }
}
