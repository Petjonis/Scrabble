/**
 * This controller class initializes the result view, depending on how many players participated in
 * the game, when a game is over.
 *
 * @author fpetek
 * @version 1.0
 */

package controller;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Player;

public class ResultController implements Initializable {

  public static ResultController resultController;

  private ArrayList<Player> players = new ArrayList<Player>();

  @FXML private FontAwesomeIconView goldStar;

  @FXML private FontAwesomeIconView silverStar;

  @FXML private FontAwesomeIconView bronzeStar;

  @FXML private FontAwesomeIconView trash;

  @FXML private Label firstPlace;

  @FXML private Label secondPlace;

  @FXML private Label thirdPlace;

  @FXML private Label fourthPlace;

  @FXML private Label firstScore;

  @FXML private Label secondScore;

  @FXML private Label thirdScore;

  @FXML private Label fourthScore;

  /**
   * Initializes "Result" view.
   *
   * @param url Gets called automatically.
   * @param resourceBundle Gets called automatically.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    resultController = this;
  }

  /**
   * Method to initialize the leaderboard labels and score value.
   * Sets places invisible if not enough players participated.
   */
  public void setResults() {
    Collections.sort(players, Player.Score);
    switch (this.players.size()) {
      case 2:
        bronzeStar.setVisible(false);
        thirdPlace.setVisible(false);
        thirdScore.setVisible(false);
        trash.setVisible(false);
        fourthPlace.setVisible(false);
        fourthScore.setVisible(false);

        firstPlace.setText(players.get(0).getUserName());
        firstScore.setText(Integer.toString(players.get(0).getScore()));
        secondPlace.setText(players.get(1).getUserName());
        secondScore.setText(Integer.toString(players.get(1).getScore()));

        break;
      case 3:
        trash.setVisible(false);
        fourthPlace.setVisible(false);
        fourthScore.setVisible(false);

        firstPlace.setText(players.get(0).getUserName());
        firstScore.setText(Integer.toString(players.get(0).getScore()));
        secondPlace.setText(players.get(1).getUserName());
        secondScore.setText(Integer.toString(players.get(1).getScore()));
        thirdPlace.setText(players.get(2).getUserName());
        thirdScore.setText(Integer.toString(players.get(2).getScore()));
        break;
      case 4:
        firstPlace.setText(players.get(0).getUserName());
        firstScore.setText(Integer.toString(players.get(0).getScore()));
        secondPlace.setText(players.get(1).getUserName());
        secondScore.setText(Integer.toString(players.get(1).getScore()));
        thirdPlace.setText(players.get(2).getUserName());
        thirdScore.setText(Integer.toString(players.get(2).getScore()));
        fourthPlace.setText(players.get(3).getUserName());
        fourthScore.setText(Integer.toString(players.get(3).getScore()));
        break;
      default:
        break;
    }
  }

  /**
   * Method to update a users database entries of gameCount, winCount, loseCount and avgScore.
   */
  public void addResultsToDatabase() {
    MainController.mainController.db.connect();
    String user = MainController.mainController.getUser().getUserName();
    int index = getIndex(MainController.mainController.getUser());

    MainController.mainController.db.updateScore(
        user,
        (MainController.mainController.db.getScore(user)
                    * MainController.mainController.db.getGames(user)
                + players.get(index).getScore())
            / (MainController.mainController.db.getGames(user) + 1));
    if (index == 0) {
      MainController.mainController.db.incrementWins(user);
    } else {
      MainController.mainController.db.incrementLoses(user);
    }

    MainController.mainController.db.disconnect();
  }

  /**
   * Method to get the users index out of the gameSessions playerlist.
   *
   * @param player The user who is playing the game.
   * @return Returns the users index of the playerlist.
   */
  private int getIndex(Player player) {
    for (int i = 0; i < players.size(); i++) {
      if (player.getPlayerID() == players.get(i).getPlayerID()) {
        return i;
      }
    }
    return -1;
  }


  public void setPlayers(ArrayList<Player> players) {
    this.players = players;
  }
}
