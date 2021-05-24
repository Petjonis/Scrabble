package messages;

import model.Player;
import java.util.ArrayList;

public class ResultPlayerListMessage extends Message {

		private static final long serialVersionUID = 1L;
		private ArrayList<Player> activePlayers;

		/**
		 * constructor for message.
		 *
		 * @param player
		 * @param newList updated player list
		 */
  public ResultPlayerListMessage(Player player, ArrayList<Player> newList) {
			super(MessageType.RESULT_MESSAGE, player);
			this.activePlayers = newList;
		}

		public ArrayList<Player> getActivePlayers() {
			return this.activePlayers;
		}
	}
