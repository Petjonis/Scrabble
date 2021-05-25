/**
 * This controller class initializes the result view, depending on
 * how many players participated in the game, when a game is over.
 *
 * @author fpetek
 * @version 1.0
 */
package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    resultController = this;

    switch (this.players.size()) {
      case 1:
        silverStar.setVisible(false);
        secondPlace.setVisible(false);
        secondScore.setVisible(false);
        bronzeStar.setVisible(false);
        thirdPlace.setVisible(false);
        thirdScore.setVisible(false);
        trash.setVisible(false);
        fourthPlace.setVisible(false);
        fourthScore.setVisible(false);

        firstPlace.setText(players.get(0).getUserName());
        firstScore.setText(Integer.toString(players.get(0).getScore()));
        break;
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

  public void getPlayers(ArrayList<Player> players) {
    this.players = players;
  }
}
